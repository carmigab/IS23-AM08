from constants import *

#shelf         is a Java Object contained in GameInfo(PlayerInfo) of type Tile[][]
#personal_goal is a Java Object contained in GameInfo(PlayerInfo) of type List<SingleGoal> (SingleGoal contains Position and TileColor)
def shelf_fitness_evaluation(shelf, personal_goal, colors_map):
    fitness=0.0

    for j in range(COLUMNS_NUMBER):

        i=ROWS_NUMBER-1
        tile=shelf[i][j]
        while not tile.isEmpty() and i>0:
            fitness=fitness+tile_fitness_evaluation(shelf, i, j, colors_map)
            i=i-1
            tile=shelf[i][j]

    for single_goal in personal_goal:
        if not shelf[single_goal.getPosition().y()][single_goal.getPosition().x()].isEmpty():
            if colors_map[shelf[single_goal.getPosition().y()][single_goal.getPosition().x()]]==colors_map[single_goal.getColor()]:
                fitness=fitness+FITNESS_PG_ACHIEVED
            else:
                fitness=fitness+FITNESS_PG_FAILED

    return fitness

def tile_fitness_evaluation(shelf, i, j, colors_map):
    fitness=FITNESS_FULL_VALUE

    if i>0:
        fitness=fitness+evaluate_neighbour(shelf[i-1][j], shelf[i][j], colors_map)
    if i<ROWS_NUMBER-1:
        fitness=fitness+evaluate_neighbour(shelf[i+1][j], shelf[i][j], colors_map)
    if j>0:
        fitness=fitness+evaluate_neighbour(shelf[i][j-1], shelf[i][j], colors_map)
    if j<COLS_NUMBER-1:
        fitness=fitness+evaluate_neighbour(shelf[i][j+1], shelf[i][j], colors_map)

    return fitness

#Java object of type Tile
def evaluate_neighbour(neighbour, tile, colors_map):
    if not neighbour.isEmpty():
        if same_color(neighbour, tile, colors_map):
            return FITNESS_SAME_NEIGHBOUR
        else:
            return FITNESS_DIFF_NEIGHBOUR
    return 0.0

#Java object of type Tile
def same_color(tile1, tile2, colors_map):
    return colors_map[tile1.getColor()]==colors_map[tile2.getColor()]