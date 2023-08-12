#pip install py4j
from py4j.java_gateway import JavaGateway
from py4j.java_collections import SetConverter, MapConverter, ListConverter
#import numpy as np
import torch
import torch.nn as nn
from constants import *
from fitness_functions import shelf_fitness_evaluation
from itertools import product
import torch.optim as optim
import time

########################################################################################################################

gateway = JavaGateway()
listConverter = ListConverter()
nicknames = ["Pippo", "Topolino"]
server=gateway.entry_point
random=gateway.jvm.java.util.Random()
currentPlayer=0

tilecolor=gateway.jvm.it.polimi.ingsw.model.TileColor

colors_map={
    tilecolor.GREEN : 0,
    tilecolor.WHITE : 1,
    tilecolor.BLUE  : 2,
    tilecolor.YELLOW: 3,
    tilecolor.CYAN  : 4,
    tilecolor.VIOLET: 5
    }
colors_map_reversed={
    0 : tilecolor.GREEN,
    1 : tilecolor.WHITE,
    2 : tilecolor.BLUE,
    3 : tilecolor.YELLOW,
    4 : tilecolor.CYAN,
    5 : tilecolor.VIOLET,
    }

colors_array=[0,1,2,3,4,5]

########################################################################################################################

class Tile:

    isInvalid=False
    isEmpty=False
    color=0

    def set_empty(self):
        self.isEmpty=True
        self.isInvalid=False
        self.color=0

    def set_invalid(self):
        self.isInvalid=True
        self.isEmpty=False
        self.color=0


    def set_color(self,color_number):
        self.color=color_number
        self.isInvalid=False
        self.isEmpty=False

class GameBoard:
    tiles=[]

    def __init__(self, game_board):
        for y in range(BOARD_DIMENSION):
            self.tiles.append([])
            for x in range(BOARD_DIMENSION):
                self.tiles[y].append(Tile())
                if game_board[y][x].isInvalid():
                    self.tiles[y][x].set_invalid()
                elif game_board[y][x].isEmpty():
                    self.tiles[y][x].set_empty()
                else:
                    self.tiles[y][x].set_color(colors_map[game_board[y][x].getColor()])

    def has_to_be_filled(self):
        for y in range(BOARD_DIMENSION):
            for x in range(BOARD_DIMENSION):
                if not self.tiles[y][x].isInvalid and not self.tiles[y][x].isEmpty:
                    #check if it has every adjacent empty
                    if not self.tiles[y][x+1].isInvalid and not self.tiles[y][x+1].isEmpty:
                        return False
                    if not self.tiles[y][x-1].isInvalid and not self.tiles[y][x-1].isEmpty:
                        return False
                    if not self.tiles[y+1][x].isInvalid and not self.tiles[y+1][x].isEmpty:
                        return False
                    if not self.tiles[y-1][x].isInvalid and not self.tiles[y+1][x].isEmpty:
                        return False
        return True



########################################################################################################################

# Hard coded, below the code that generated them

all_moves_list=[[0], [1], [2], [3], [4], [5], [0, 0], [0, 1], [0, 2], [0, 3], [0, 4], [0, 5], [1, 0], [1, 1], [1, 2], [1, 3], [1, 4], [1, 5], [2, 0], [2, 1], [2, 2], [2, 3], [2, 4], [2, 5], [3, 0], [3, 1], [3, 2], [3, 3], [3, 4], [3, 5], [4, 0], [4, 1], [4, 2], [4, 3], [4, 4], [4, 5], [5, 0], [5, 1], [5, 2], [5, 3], [5, 4], [5, 5], [0, 0, 0], [0, 0, 1], [0, 0, 2], [0, 0, 3], [0, 0, 4], [0, 0, 5], [0, 1, 0], [0, 1, 1], [0, 1, 2], [0, 1, 3], [0, 1, 4], [0, 1, 5], [0, 2, 0], [0, 2, 1], [0, 2, 2], [0, 2, 3], [0, 2, 4], [0, 2, 5], [0, 3, 0], [0, 3, 1], [0, 3, 2], [0, 3, 3], [0, 3, 4], [0, 3, 5], [0, 4, 0], [0, 4, 1], [0, 4, 2], [0, 4, 3], [0, 4, 4], [0, 4, 5], [0, 5, 0], [0, 5, 1], [0, 5, 2], [0, 5, 3], [0, 5, 4], [0, 5, 5], [1, 0, 0], [1, 0, 1], [1, 0, 2], [1, 0, 3], [1, 0, 4], [1, 0, 5], [1, 1, 0], [1, 1, 1], [1, 1, 2], [1, 1, 3], [1, 1, 4], [1, 1, 5], [1, 2, 0], [1, 2, 1], [1, 2, 2], [1, 2, 3], [1, 2, 4], [1, 2, 5], [1, 3, 0], [1, 3, 1], [1, 3, 2], [1, 3, 3], [1, 3, 4], [1, 3, 5], [1, 4, 0], [1, 4, 1], [1, 4, 2], [1, 4, 3], [1, 4, 4], [1, 4, 5], [1, 5, 0], [1, 5, 1], [1, 5, 2], [1, 5, 3], [1, 5, 4], [1, 5, 5], [2, 0, 0], [2, 0, 1], [2, 0, 2], [2, 0, 3], [2, 0, 4], [2, 0, 5], [2, 1, 0], [2, 1, 1], [2, 1, 2], [2, 1, 3], [2, 1, 4], [2, 1, 5], [2, 2, 0], [2, 2, 1], [2, 2, 2], [2, 2, 3], [2, 2, 4], [2, 2, 5], [2, 3, 0], [2, 3, 1], [2, 3, 2], [2, 3, 3], [2, 3, 4], [2, 3, 5], [2, 4, 0], [2, 4, 1], [2, 4, 2], [2, 4, 3], [2, 4, 4], [2, 4, 5], [2, 5, 0], [2, 5, 1], [2, 5, 2], [2, 5, 3], [2, 5, 4], [2, 5, 5], [3, 0, 0], [3, 0, 1], [3, 0, 2], [3, 0, 3], [3, 0, 4], [3, 0, 5], [3, 1, 0], [3, 1, 1], [3, 1, 2], [3, 1, 3], [3, 1, 4], [3, 1, 5], [3, 2, 0], [3, 2, 1], [3, 2, 2], [3, 2, 3], [3, 2, 4], [3, 2, 5], [3, 3, 0], [3, 3, 1], [3, 3, 2], [3, 3, 3], [3, 3, 4], [3, 3, 5], [3, 4, 0], [3, 4, 1], [3, 4, 2], [3, 4, 3], [3, 4, 4], [3, 4, 5], [3, 5, 0], [3, 5, 1], [3, 5, 2], [3, 5, 3], [3, 5, 4], [3, 5, 5], [4, 0, 0], [4, 0, 1], [4, 0, 2], [4, 0, 3], [4, 0, 4], [4, 0, 5], [4, 1, 0], [4, 1, 1], [4, 1, 2], [4, 1, 3], [4, 1, 4], [4, 1, 5], [4, 2, 0], [4, 2, 1], [4, 2, 2], [4, 2, 3], [4, 2, 4], [4, 2, 5], [4, 3, 0], [4, 3, 1], [4, 3, 2], [4, 3, 3], [4, 3, 4], [4, 3, 5], [4, 4, 0], [4, 4, 1], [4, 4, 2], [4, 4, 3], [4, 4, 4], [4, 4, 5], [4, 5, 0], [4, 5, 1], [4, 5, 2], [4, 5, 3], [4, 5, 4], [4, 5, 5], [5, 0, 0], [5, 0, 1], [5, 0, 2], [5, 0, 3], [5, 0, 4], [5, 0, 5], [5, 1, 0], [5, 1, 1], [5, 1, 2], [5, 1, 3], [5, 1, 4], [5, 1, 5], [5, 2, 0], [5, 2, 1], [5, 2, 2], [5, 2, 3], [5, 2, 4], [5, 2, 5], [5, 3, 0], [5, 3, 1], [5, 3, 2], [5, 3, 3], [5, 3, 4], [5, 3, 5], [5, 4, 0], [5, 4, 1], [5, 4, 2], [5, 4, 3], [5, 4, 4], [5, 4, 5], [5, 5, 0], [5, 5, 1], [5, 5, 2], [5, 5, 3], [5, 5, 4], [5, 5, 5]]
all_moves={0: [0], 1: [1], 2: [2], 3: [3], 4: [4], 5: [5], 6: [0, 0], 7: [0, 1], 8: [0, 2], 9: [0, 3], 10: [0, 4], 11: [0, 5], 12: [1, 0], 13: [1, 1], 14: [1, 2], 15: [1, 3], 16: [1, 4], 17: [1, 5], 18: [2, 0], 19: [2, 1], 20: [2, 2], 21: [2, 3], 22: [2, 4], 23: [2, 5], 24: [3, 0], 25: [3, 1], 26: [3, 2], 27: [3, 3], 28: [3, 4], 29: [3, 5], 30: [4, 0], 31: [4, 1], 32: [4, 2], 33: [4, 3], 34: [4, 4], 35: [4, 5], 36: [5, 0], 37: [5, 1], 38: [5, 2], 39: [5, 3], 40: [5, 4], 41: [5, 5], 42: [0, 0, 0], 43: [0, 0, 1], 44: [0, 0, 2], 45: [0, 0, 3], 46: [0, 0, 4], 47: [0, 0, 5], 48: [0, 1, 0], 49: [0, 1, 1], 50: [0, 1, 2], 51: [0, 1, 3], 52: [0, 1, 4], 53: [0, 1, 5], 54: [0, 2, 0], 55: [0, 2, 1], 56: [0, 2, 2], 57: [0, 2, 3], 58: [0, 2, 4], 59: [0, 2, 5], 60: [0, 3, 0], 61: [0, 3, 1], 62: [0, 3, 2], 63: [0, 3, 3], 64: [0, 3, 4], 65: [0, 3, 5], 66: [0, 4, 0], 67: [0, 4, 1], 68: [0, 4, 2], 69: [0, 4, 3], 70: [0, 4, 4], 71: [0, 4, 5], 72: [0, 5, 0], 73: [0, 5, 1], 74: [0, 5, 2], 75: [0, 5, 3], 76: [0, 5, 4], 77: [0, 5, 5], 78: [1, 0, 0], 79: [1, 0, 1], 80: [1, 0, 2], 81: [1, 0, 3], 82: [1, 0, 4], 83: [1, 0, 5], 84: [1, 1, 0], 85: [1, 1, 1], 86: [1, 1, 2], 87: [1, 1, 3], 88: [1, 1, 4], 89: [1, 1, 5], 90: [1, 2, 0], 91: [1, 2, 1], 92: [1, 2, 2], 93: [1, 2, 3], 94: [1, 2, 4], 95: [1, 2, 5], 96: [1, 3, 0], 97: [1, 3, 1], 98: [1, 3, 2], 99: [1, 3, 3], 100: [1, 3, 4], 101: [1, 3, 5], 102: [1, 4, 0], 103: [1, 4, 1], 104: [1, 4, 2], 105: [1, 4, 3], 106: [1, 4, 4], 107: [1, 4, 5], 108: [1, 5, 0], 109: [1, 5, 1], 110: [1, 5, 2], 111: [1, 5, 3], 112: [1, 5, 4], 113: [1, 5, 5], 114: [2, 0, 0], 115: [2, 0, 1], 116: [2, 0, 2], 117: [2, 0, 3], 118: [2, 0, 4], 119: [2, 0, 5], 120: [2, 1, 0], 121: [2, 1, 1], 122: [2, 1, 2], 123: [2, 1, 3], 124: [2, 1, 4], 125: [2, 1, 5], 126: [2, 2, 0], 127: [2, 2, 1], 128: [2, 2, 2], 129: [2, 2, 3], 130: [2, 2, 4], 131: [2, 2, 5], 132: [2, 3, 0], 133: [2, 3, 1], 134: [2, 3, 2], 135: [2, 3, 3], 136: [2, 3, 4], 137: [2, 3, 5], 138: [2, 4, 0], 139: [2, 4, 1], 140: [2, 4, 2], 141: [2, 4, 3], 142: [2, 4, 4], 143: [2, 4, 5], 144: [2, 5, 0], 145: [2, 5, 1], 146: [2, 5, 2], 147: [2, 5, 3], 148: [2, 5, 4], 149: [2, 5, 5], 150: [3, 0, 0], 151: [3, 0, 1], 152: [3, 0, 2], 153: [3, 0, 3], 154: [3, 0, 4], 155: [3, 0, 5], 156: [3, 1, 0], 157: [3, 1, 1], 158: [3, 1, 2], 159: [3, 1, 3], 160: [3, 1, 4], 161: [3, 1, 5], 162: [3, 2, 0], 163: [3, 2, 1], 164: [3, 2, 2], 165: [3, 2, 3], 166: [3, 2, 4], 167: [3, 2, 5], 168: [3, 3, 0], 169: [3, 3, 1], 170: [3, 3, 2], 171: [3, 3, 3], 172: [3, 3, 4], 173: [3, 3, 5], 174: [3, 4, 0], 175: [3, 4, 1], 176: [3, 4, 2], 177: [3, 4, 3], 178: [3, 4, 4], 179: [3, 4, 5], 180: [3, 5, 0], 181: [3, 5, 1], 182: [3, 5, 2], 183: [3, 5, 3], 184: [3, 5, 4], 185: [3, 5, 5], 186: [4, 0, 0], 187: [4, 0, 1], 188: [4, 0, 2], 189: [4, 0, 3], 190: [4, 0, 4], 191: [4, 0, 5], 192: [4, 1, 0], 193: [4, 1, 1], 194: [4, 1, 2], 195: [4, 1, 3], 196: [4, 1, 4], 197: [4, 1, 5], 198: [4, 2, 0], 199: [4, 2, 1], 200: [4, 2, 2], 201: [4, 2, 3], 202: [4, 2, 4], 203: [4, 2, 5], 204: [4, 3, 0], 205: [4, 3, 1], 206: [4, 3, 2], 207: [4, 3, 3], 208: [4, 3, 4], 209: [4, 3, 5], 210: [4, 4, 0], 211: [4, 4, 1], 212: [4, 4, 2], 213: [4, 4, 3], 214: [4, 4, 4], 215: [4, 4, 5], 216: [4, 5, 0], 217: [4, 5, 1], 218: [4, 5, 2], 219: [4, 5, 3], 220: [4, 5, 4], 221: [4, 5, 5], 222: [5, 0, 0], 223: [5, 0, 1], 224: [5, 0, 2], 225: [5, 0, 3], 226: [5, 0, 4], 227: [5, 0, 5], 228: [5, 1, 0], 229: [5, 1, 1], 230: [5, 1, 2], 231: [5, 1, 3], 232: [5, 1, 4], 233: [5, 1, 5], 234: [5, 2, 0], 235: [5, 2, 1], 236: [5, 2, 2], 237: [5, 2, 3], 238: [5, 2, 4], 239: [5, 2, 5], 240: [5, 3, 0], 241: [5, 3, 1], 242: [5, 3, 2], 243: [5, 3, 3], 244: [5, 3, 4], 245: [5, 3, 5], 246: [5, 4, 0], 247: [5, 4, 1], 248: [5, 4, 2], 249: [5, 4, 3], 250: [5, 4, 4], 251: [5, 4, 5], 252: [5, 5, 0], 253: [5, 5, 1], 254: [5, 5, 2], 255: [5, 5, 3], 256: [5, 5, 4], 257: [5, 5, 5]}


# iteration=0;
#
# # 1 tile iteration
#
# for color in colors_map:
#     templist=[]
#     templist.append(colors_map[color])
#     all_moves_list.append(templist)
#     all_moves[iteration]=templist
#     iteration=iteration+1
#
# # 2 tile iteration
#
# for color in product(all_moves_list, colors_array):
#     templist=[]
#
#     for element in color:
#         if isinstance(element, list):
#             for element_reduced in element:
#                 templist.append(element_reduced)
#         else:
#             templist.append(element)
#
#     all_moves_list.append(templist)
#     all_moves[iteration]=templist
#     iteration=iteration+1
#
# # 3 tile iteration
#
# for color in product(all_moves_list, colors_array):
#     templist=[]
#
#     for element in color:
#         if isinstance(element, list):
#             for element_reduced in element:
#                 templist.append(element_reduced)
#         else:
#             templist.append(element)
#
#     if len(templist)>2:
#         all_moves_list.append(templist)
#         all_moves[iteration]=templist
#         iteration=iteration+1
#
# iteration=0

########################################################################################################################

mask_single_move=[1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
mask_double_move=[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]

mask_available_columns=[]
mask_available_actions=[]

outputs=[]

def setup_mask_available_columns():
    for i in range(COLUMNS_NUMBER):
            mask_available_columns.append(0)

def setup_mask_available_actions():
    for i in range(len(all_moves)):
        mask_available_actions.append([0, [[],[],[]]])

def reset_mask_available_acitons():
    for i in range(len(all_moves)):
        mask_available_actions[i]=[0, [[],[],[]]]

setup_mask_available_columns()
setup_mask_available_actions()

#it is available if it is not full
def calculate_mask_available_columns(shelf):

    for i in range(COLUMNS_NUMBER):
        if shelf[0][i].isEmpty():
            mask_available_columns[i]=1
        else:
            mask_available_columns[i]=0

    return mask_available_columns

def calculate_mask_available_actions_optimized(gameboard):

    reset_mask_available_acitons()

    #Search first the single positions

    #[Y,X]
    #Y ROWS
    #X COLUMNS

    list_all_moves_keys=list(all_moves.keys())
    list_all_moves_values=list(all_moves.values())


    for y in range(BOARD_DIMENSION):
        for x in range(BOARD_DIMENSION):
            if not gameboard.tiles[y][x].isEmpty and not gameboard.tiles[y][x].isInvalid:

                neighbours=[]
                count =0

                #Check its neigbours and see if it is vaild
                #note that in a game with 2 players you do not need to check if it is in an edge (just to save some time in training)

                if not(gameboard.tiles[y][x+1].isEmpty or gameboard.tiles[y][x+1].isInvalid):
                    neighbours.append([0,1])
                    count=count+1
                if not(gameboard.tiles[y][x-1].isEmpty or gameboard.tiles[y][x-1].isInvalid):
                    neighbours.append([0,-1])
                    count=count+1
                if not(gameboard.tiles[y+1][x].isEmpty or gameboard.tiles[y+1][x].isInvalid):
                    neighbours.append([1,0])
                    count=count+1
                if not(gameboard.tiles[y-1][x].isEmpty or gameboard.tiles[y-1][x].isInvalid):
                    neighbours.append([-1,0])
                    count=count+1

                #if it has every potential then it is not a valid move because it has every neighbour
                if not count==4:

                    #then add it as a single move

                    color_mapped=gameboard.tiles[y][x].color

                    idx1=list_all_moves_keys[list_all_moves_values.index([color_mapped])]

                    mask_available_actions[idx1][0]=1
                    mask_available_actions[idx1][1][0]=[x,y]

                    #and check its neighbours because they could be double moves

                    for neighbour in neighbours:

                        neighbours_neighbour=0

                        potential_triple=False

                        #we check every neighbour except the one we came from



                        #this means we have to move in the y direction
                        if neighbour[0]!=0:
                            if not(gameboard.tiles[y+neighbour[0]][x+neighbour[0]].isEmpty or gameboard.tiles[y+neighbour[0]][x+neighbour[0]].isInvalid):
                                neighbours_neighbour=neighbours_neighbour+1
                            if not(gameboard.tiles[y+neighbour[0]][x-neighbour[0]].isEmpty or gameboard.tiles[y+neighbour[0]][x-neighbour[0]].isInvalid):
                                neighbours_neighbour=neighbours_neighbour+1
                            if not(gameboard.tiles[y+neighbour[0]*2][x].isEmpty or gameboard.tiles[y+neighbour[0]*2][x].isInvalid):
                                neighbours_neighbour=neighbours_neighbour+1
                                potential_triple=True
                        #or else we move in the x direction and do the same checks
                        else:
                            if not(gameboard.tiles[y+neighbour[1]][x+neighbour[1]].isEmpty or gameboard.tiles[y+neighbour[1]][x+neighbour[1]].isInvalid):
                                neighbours_neighbour=neighbours_neighbour+1
                            if not(gameboard.tiles[y-neighbour[1]][x+neighbour[1]].isEmpty or gameboard.tiles[y-neighbour[1]][x+neighbour[1]].isInvalid):
                                neighbours_neighbour=neighbours_neighbour+1
                            if not(gameboard.tiles[y][x+neighbour[1]*2].isEmpty or gameboard.tiles[y][x+neighbour[1]*2].isInvalid):
                                neighbours_neighbour=neighbours_neighbour+1
                                potential_triple=True

                        #if not every neighbour is occupied then it is a valid move to add
                        if neighbours_neighbour!=3:

                            color_first_neighbour_mapped=gameboard.tiles[y+neighbour[0]][x+neighbour[1]].color

                            idx2=list_all_moves_keys[list_all_moves_values.index([color_mapped, color_first_neighbour_mapped])]

                            mask_available_actions[idx2][0]=1
                            mask_available_actions[idx2][1][0]=[x,y]
                            mask_available_actions[idx2][1][1]=[x+neighbour[1],y+neighbour[0]]

                            #if it is a potential triple then we need to check the neighbours in that direction too
                            if potential_triple:

                                neighbours_neighbour=0

                                #we check every neighbour except the one we came from

                                #this means we have to move in the y direction
                                if neighbour[0]!=0:
                                    if not(gameboard.tiles[y+neighbour[0]*2][x+neighbour[0]].isEmpty or gameboard.tiles[y+neighbour[0]*2][x+neighbour[0]].isInvalid):
                                        neighbours_neighbour=neighbours_neighbour+1
                                    if not(gameboard.tiles[y+neighbour[0]*2][x-neighbour[0]].isEmpty or gameboard.tiles[y+neighbour[0]*2][x-neighbour[0]].isInvalid):
                                        neighbours_neighbour=neighbours_neighbour+1
                                    if not(gameboard.tiles[y+neighbour[0]*3][x].isEmpty or gameboard.tiles[y+neighbour[0]*3][x].isInvalid):
                                        neighbours_neighbour=neighbours_neighbour+1
                                #or else we move in the x direction and do the same checks
                                else:
                                    if not(gameboard.tiles[y+neighbour[1]][x+neighbour[1]*2].isEmpty or gameboard.tiles[y+neighbour[1]][x+neighbour[1]*2].isInvalid):
                                        neighbours_neighbour=neighbours_neighbour+1
                                    if not(gameboard.tiles[y-neighbour[1]][x+neighbour[1]*2].isEmpty or gameboard.tiles[y-neighbour[1]][x+neighbour[1]*2].isInvalid):
                                        neighbours_neighbour=neighbours_neighbour+1
                                    if not(gameboard.tiles[y][x+neighbour[1]*3].isEmpty or gameboard.tiles[y][x+neighbour[1]*3].isInvalid):
                                        neighbours_neighbour=neighbours_neighbour+1

                                #if not every neighbour is occupied then it is a valid move to add
                                if neighbours_neighbour!=3:

                                    color_second_neighbour_mapped=gameboard.tiles[y+neighbour[0]*2][x+neighbour[1]*2].color

                                    idx3=list_all_moves_keys[list_all_moves_values.index([color_mapped, color_first_neighbour_mapped, color_second_neighbour_mapped])]

                                    mask_available_actions[idx3][0]=1
                                    mask_available_actions[idx3][1][0]=[x,y]
                                    mask_available_actions[idx3][1][1]=[x+neighbour[1],y+neighbour[0]]
                                    mask_available_actions[idx3][1][2]=[x+neighbour[1]*2,y+neighbour[0]*2]

                                    #i am missing all the other 4 permutations which are not seen by the algorithm

                                    idx4=list_all_moves_keys[list_all_moves_values.index([color_mapped, color_second_neighbour_mapped, color_first_neighbour_mapped])]
                                    idx5=list_all_moves_keys[list_all_moves_values.index([color_second_neighbour_mapped, color_mapped, color_first_neighbour_mapped])]
                                    idx6=list_all_moves_keys[list_all_moves_values.index([color_first_neighbour_mapped, color_mapped, color_second_neighbour_mapped])]
                                    idx7=list_all_moves_keys[list_all_moves_values.index([color_first_neighbour_mapped, color_second_neighbour_mapped, color_mapped])]

                                    if mask_available_actions[idx4][0]==0:
                                        mask_available_actions[idx4][0]=1
                                        mask_available_actions[idx4][1][0]=[x,y]
                                        mask_available_actions[idx4][1][1]=[x+neighbour[1]*2,y+neighbour[0]*2]
                                        mask_available_actions[idx4][1][2]=[x+neighbour[1],y+neighbour[0]]
                                    if mask_available_actions[idx5][0]==0:
                                        mask_available_actions[idx5][0]=1
                                        mask_available_actions[idx5][1][0]=[x+neighbour[1]*2,y+neighbour[0]*2]
                                        mask_available_actions[idx5][1][1]=[x,y]
                                        mask_available_actions[idx5][1][2]=[x+neighbour[1],y+neighbour[0]]
                                    if mask_available_actions[idx6][0]==0:
                                        mask_available_actions[idx6][0]=1
                                        mask_available_actions[idx6][1][0]=[x+neighbour[1],y+neighbour[0]]
                                        mask_available_actions[idx6][1][1]=[x,y]
                                        mask_available_actions[idx6][1][2]=[x+neighbour[1]*2,y+neighbour[0]*2]
                                    if mask_available_actions[idx7][0]==0:
                                        mask_available_actions[idx7][0]=1
                                        mask_available_actions[idx7][1][0]=[x+neighbour[1],y+neighbour[0]]
                                        mask_available_actions[idx7][1][1]=[x+neighbour[1]*2,y+neighbour[0]*2]
                                        mask_available_actions[idx7][1][2]=[x,y]




def calculate_mask_available_actions(gameboard):

    reset_mask_available_acitons()

    single_available_positions=get_adj([], gameboard)

    for position in single_available_positions:

        color_mapped=colors_map[gameboard[position[1]][position[0]].getColor()]

        idx1=list(all_moves.keys())[list(all_moves.values()).index([color_mapped])]

        mask_available_actions[idx1][0]=1
        mask_available_actions[idx1][1][0]=position

        double_available_positions=get_adj([position], gameboard)

        for first_neighbour in double_available_positions:

            color_first_neighbour_mapped=colors_map[gameboard[first_neighbour[1]][first_neighbour[0]].getColor()]

            idx2=list(all_moves.keys())[list(all_moves.values()).index([color_mapped, color_first_neighbour_mapped])]

            mask_available_actions[idx2][0]=1
            mask_available_actions[idx2][1][0]=position
            mask_available_actions[idx2][1][1]=first_neighbour

            triple_available_positions=get_adj([position, first_neighbour], gameboard)

            for second_neighbour in triple_available_positions:

                color_second_neighbour_mapped=colors_map[gameboard[second_neighbour[1]][second_neighbour[0]].getColor()]

                idx3=list(all_moves.keys())[list(all_moves.values()).index([color_mapped, color_first_neighbour_mapped, color_second_neighbour_mapped])]

                mask_available_actions[idx3][0]=1
                mask_available_actions[idx3][1][0]=position
                mask_available_actions[idx3][1][1]=first_neighbour
                mask_available_actions[idx3][1][2]=second_neighbour

    return mask_available_actions

def get_adj(positions, gameboard):

    result=[]

    if len(positions)==0:
        for i in range(BOARD_DIMENSION):
            for j in range(BOARD_DIMENSION):
                result.append([i, j])

    elif len(positions)==1:
        result.append([(positions[0])[0], (positions[0])[1]+1])
        result.append([(positions[0])[0], (positions[0])[1]-1])
        result.append([(positions[0])[0]+1, (positions[0])[1]])
        result.append([(positions[0])[0]-1, (positions[0])[1]])

    else:
        if (positions[0])[0]==(positions[1])[0]:
            result.append([(positions[0])[0], min([(positions[0])[1]-1,(positions[1])[1]-1])])
            result.append([(positions[0])[0], max([(positions[0])[1]+1,(positions[1])[1]+1])])
        else:
            result.append([min([(positions[0])[0]-1,(positions[1])[0]-1]), (positions[0])[1]])
            result.append([max([(positions[0])[0]+1,(positions[1])[0]+1]), (positions[0])[1]])

    return reduce_adjacent(result, gameboard)

def reduce_adjacent(adjacent, gameboard):

    result=[]

    for element in adjacent:
        if check_single_position(element, gameboard):
            result.append(element)

    return result

def check_single_position(position, gameboard):

    if position[0]<0 or position[0]>BOARD_DIMENSION:
        return False
    if position[1]<0 or position[1]>BOARD_DIMENSION:
        return False
    if gameboard[position[1]][position[0]].isEmpty() or gameboard[position[1]][position[0]].isInvalid():
        return False

    return has_free_adjacent(position, gameboard)

def has_free_adjacent(position, gameboard):

    adjacents=get_adjacent_positions(position)

    if len(adjacents)<4:
        return True
    for pos in adjacents:
        if gameboard[pos[1]][pos[0]].isEmpty() or gameboard[pos[1]][pos[0]].isInvalid():
            return True

    return False

def get_adjacent_positions(position):

    adjacent_positions=[]

    if position[0] < BOARD_DIMENSION-1:
        adjacent_positions.append([position[0]+1, position[1]])
    if position[1] < BOARD_DIMENSION-1:
        adjacent_positions.append([position[0], position[1]+1])
    if position[0] > 0:
        adjacent_positions.append([position[0]-1, position[1]])
    if position[1] > 0:
        adjacent_positions.append([position[0], position[1]-1])

    return adjacent_positions


########################################################################################################################

model = nn.Sequential(
    nn.Linear(INPUT_SIZE, OUTPUT_SIZE, bias=False),
    nn.Sigmoid()
)

print(model)

#TODO backpropagation, save results, fix selection of moves (in particular column selection when is full)

########################################################################################################################

gameinfo=server.getCurrentGameInfo()

gameboard=GameBoard(gameinfo.getGameBoard())

# calculate_mask_available_actions_optimized(gameinfo.getGameBoard())
#
# print("Mask available actions")
# str=""
# for action in mask_available_actions:
#     if action[0]==0:
#         str=str+"0 "
#     else:
#         str=str+"1 "
# print(str)
# print(mask_available_actions)
#
# calculate_mask_available_actions(gameinfo.getGameBoard())
#
# print("Mask available actions")
# str=""
# for action in mask_available_actions:
#     if action[0]==0:
#         str=str+"0 "
#     else:
#         str=str+"1 "
# print(str)
# print(mask_available_actions)

start_time=time.time()

while not server.isGameEnded():
    if gameinfo.getCurrentPlayerNickname() == nicknames[currentPlayer]:

        #######################
        # GET THE GAME INFORMATION
        #######################

        gameinfo=server.getCurrentGameInfo()

        #######################
        # PREPARE THE MASK FOR THE AVAILABLE ACTIONS
        #######################

        # print("Elapsed time 1: %f" %( time.time()-start_time) )
        #
        # for i in range(BOARD_DIMENSION):
        #     str=""
        #     for j in range(BOARD_DIMENSION):
        #         if gameinfo.getGameBoard()[i][j].isInvalid():
        #             str=str+" 0"
        #         elif gameinfo.getGameBoard()[i][j].isEmpty():
        #             str=str+" 1"
        #         else:
        #             str=str+" 2"
        #     print(str)
        #
        # print("--")

        if gameboard.has_to_be_filled():
            gameboard=GameBoard(gameinfo.getGameBoard())

        # for i in range(BOARD_DIMENSION):
        #     str=""
        #     for j in range(BOARD_DIMENSION):
        #         if gameboard.tiles[i][j].isInvalid:
        #             str=str+" 0"
        #         elif gameboard.tiles[i][j].isEmpty:
        #             str=str+" 1"
        #         else:
        #             str=str+" 2"
        #     print(str)

        calculate_mask_available_actions_optimized(gameboard)
        calculate_mask_available_columns(gameinfo.getPlayerInfosList().get(currentPlayer).getShelf())

        # print("Mask available actions")
        # print(mask_available_actions)

        #######################
        # PREPARE THE INPUTS
        #######################



        inputs=[]
        for i in range(INPUT_SIZE):
            inputs.append(0)

        inputs[gameinfo.getPlayerInfosList().get(0).getPersonalGoalNumber()]=1

        inputs[PERSONAL_GOALS+gameinfo.getCommonGoalsCreated().get(0)]=1
        inputs[PERSONAL_GOALS+gameinfo.getCommonGoalsCreated().get(1)]=1

        inputs=torch.tensor(inputs, dtype=torch.float32)

        #print(inputs)

        #######################
        # CALCULATE THE OUTPUTS WITH THE FEEDFORWARD AND TRANSFORM IT TO POSITIONS ON THE BOARD
        #######################

        outputs=model(inputs).tolist()


        #print(outputs)

        for i in range(len(all_moves)):
            outputs[i]=outputs[i]*mask_available_actions[i][0]

        for i in range(len(all_moves), len(outputs)):
            outputs[i]=outputs[i]*mask_available_columns[i-len(all_moves)]

        #print(mask_available_columns)

        #print(outputs)

        outputs_move=[]
        outputs_cols=[]

        for i in range(len(all_moves)):
            outputs_move.append(outputs[i])

        for i in range(len(all_moves), len(outputs)):
            outputs_cols.append(outputs[i])

        best_colors=outputs_move.index(max(outputs_move))
        #print("Best move")
        #print(best_colors)
        best_column=outputs_cols.index(max(outputs_cols))
        #print("Best column")
        #print(best_column)

        best_move=mask_available_actions[best_colors][1]

        free_spaces=0

        while free_spaces!=ROWS_NUMBER and gameinfo.getPlayerInfosList().get(currentPlayer).getShelf()[free_spaces][best_column].isEmpty() :
            free_spaces=free_spaces+1

        #print(free_spaces)

        diff=free_spaces - len(best_move)

        #print("Diff")
        #print(diff)

        if diff<0:
            if diff==-1 and free_spaces==2:
                for i in range(len(all_moves)):
                    outputs[i]=outputs[i]*mask_double_move[i]
            else:
                for i in range(len(all_moves)):
                    outputs[i]=outputs[i]*mask_single_move[i]

            outputs_move=[]
            outputs_cols=[]

            for i in range(len(all_moves)):
                outputs_move.append(outputs[i])

            for i in range(len(all_moves), len(outputs)):
                outputs_cols.append(outputs[i])

            best_colors=outputs_move.index(max(outputs_move))
            #print("New best move")
            #print(best_colors)
            best_column=outputs_cols.index(max(outputs_cols))
            #print("New best column")
            #print(best_column)

            best_move=mask_available_actions[best_colors][1]

        # print("Best position on the board")
        # for pos in best_move:
        #     if pos:
        #         print(pos[0])
        #         print(pos[1])
        #         print("-")

        #######################
        # EVALUATE THE FITNESS BEFORE
        #######################

        #print("Fitness")
        #print(shelf_fitness_evaluation(gameinfo.getPlayerInfosList().get(0).getShelf(), gameinfo.getPlayerInfosList().get(0).getPersonalGoal(), colors_map))

        #######################
        # MAKE THE MOVE ON THE SERVER
        #######################

        list_to_java=[]
        for pos in best_move:
            if pos:
                list_to_java.append(gateway.jvm.it.polimi.ingsw.model.Position(pos[0],pos[1]))

                #when we have done the move we can remove the positions from our gameboard
                gameboard.tiles[pos[1]][pos[0]].set_empty()

        # print(list_to_java)

        # for i in range(ROWS_NUMBER):
        #     str=""
        #     for j in range(COLUMNS_NUMBER):
        #         if gameinfo.getPlayerInfosList().get(currentPlayer).getShelf()[i][j].isInvalid():
        #             str=str+" 0"
        #         elif gameinfo.getPlayerInfosList().get(currentPlayer).getShelf()[i][j].isEmpty():
        #             str=str+" 1"
        #         else:
        #             str=str+" 2"
        #     print(str)

        server.makeMove(
            ListConverter().convert(list_to_java, gateway._gateway_client),
            best_column,
            nicknames[currentPlayer])

        #######################
        # GET NEW INFORMATION AND EVALUATE FITNESS AFTER
        #######################

        gameinfo=server.getCurrentGameInfo()

        #print("New fitness")
        #print(shelf_fitness_evaluation(gameinfo.getPlayerInfosList().get(0).getShelf(), gameinfo.getPlayerInfosList().get(0).getPersonalGoal(), colors_map))

        #######################
        # BACKPROPAGATE
        #######################

        #TODO

        #######################
        # SAVE SCORES
        #######################

        #TODO


    currentPlayer=(currentPlayer+1)%2

print("Elapsed time END: %f" %( time.time()-start_time) )
print("Game done")