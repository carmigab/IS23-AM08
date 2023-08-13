from constants import *

class Tile:

    def __init__(self):
        self.isEmpty=False
        self.isInvalid=False
        self.color=0

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

    def __init__(self, game_board, colors_map):
        self.tiles=[]
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
                    if not self.tiles[y-1][x].isInvalid and not self.tiles[y-1][x].isEmpty:
                        return False
        return True

class Shelf:

    def __init__(self):
        self.tiles=[]
        for y in range(ROWS_NUMBER):
            self.tiles.append([])
            for x in range(COLUMNS_NUMBER):
                self.tiles[y].append(Tile())
                self.tiles[y][x].set_empty()

    #move is a list of colors
    def make_move(self, move, column):

        row_to_insert=ROWS_NUMBER-1

        while not self.tiles[row_to_insert][column].isEmpty:
            row_to_insert=row_to_insert-1

        for single in move:
            self.tiles[row_to_insert][column].set_color(single)
            row_to_insert=row_to_insert-1

class PersonalGoal:

    #it contains a list of positions and colors
    def __init__(self, personal_goal, colors_map):
        self.single_goals=[]
        for single_goal in personal_goal:
            self.single_goals.append([[single_goal.getPosition().x(), single_goal.getPosition().y()], colors_map[single_goal.getColor()]])
