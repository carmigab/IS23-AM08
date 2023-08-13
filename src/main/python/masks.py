from constants import *

mask_single_move=[1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
mask_double_move=[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]

mask_available_columns=[]
mask_available_actions=[]

outputs=[]

def setup_mask_available_columns():
    for i in range(COLUMNS_NUMBER):
        mask_available_columns.append(0)

def setup_mask_available_actions():
    for i in range(ALL_POSSIBLE_MOVES):
        mask_available_actions.append([0, [[],[],[]]])

def reset_mask_available_acitons():
    for i in range(ALL_POSSIBLE_MOVES):
        mask_available_actions[i]=[0, [[],[],[]]]

#it is available if it is not full
def calculate_mask_available_columns(shelf):

    for i in range(COLUMNS_NUMBER):
        if shelf[0][i].isEmpty():
            mask_available_columns[i]=1
        else:
            mask_available_columns[i]=0

    return mask_available_columns

def calculate_mask_available_actions_optimized(gameboard, all_moves):

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

#Not optimized, do not use
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