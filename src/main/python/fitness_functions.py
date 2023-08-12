from constants import *

def shelf_fitness_evaluation(shelf, personal_goal):
    fitness=0.0

    for j in range(COLUMNS_NUMBER):

        i=ROWS_NUMBER-1
        tile=shelf.tiles[i][j]
        while not tile.isEmpty and i>0:
            fitness=fitness+tile_fitness_evaluation(shelf, i, j)
            i=i-1
            tile=shelf.tiles[i][j]

    for single_goal in personal_goal.single_goals:
        if not shelf.tiles[single_goal[0][1]][single_goal[0][0]].isEmpty:
            if shelf.tiles[single_goal[0][1]][single_goal[0][0]].color==single_goal[1]:
                fitness=fitness+FITNESS_PG_ACHIEVED
            else:
                fitness=fitness+FITNESS_PG_FAILED

    return fitness

def tile_fitness_evaluation(shelf, i, j):
    fitness=FITNESS_FULL_VALUE

    if i>0:
        fitness=fitness+evaluate_neighbour(shelf.tiles[i-1][j], shelf.tiles[i][j])
    if i<ROWS_NUMBER-1:
        fitness=fitness+evaluate_neighbour(shelf.tiles[i+1][j], shelf.tiles[i][j])
    if j>0:
        fitness=fitness+evaluate_neighbour(shelf.tiles[i][j-1], shelf.tiles[i][j])
    if j<COLUMNS_NUMBER-1:
        fitness=fitness+evaluate_neighbour(shelf.tiles[i][j+1], shelf.tiles[i][j])

    return fitness

def evaluate_neighbour(neighbour, tile):
    if not neighbour.isEmpty:
        if neighbour.color==tile.color:
            return FITNESS_SAME_NEIGHBOUR
        else:
            return FITNESS_DIFF_NEIGHBOUR
    return 0.0