# IS23-AM08

## Group members
* Gabriele Carminati
* Gabriele Carrino
* Matteo Cenzato
* Alessandro Capellino

## Introduction

This branch is used only for the study and training of the computer that plays the game.

This idea could be developed in two different ways: 

1) bruteforce calculation based on an evaluation funcion 
2) neural networks

Well... we explored both :/

## First Idea: brute force

The idea behind 1, is that we have a function that estimates how good the position is based on:

- number of adjacent tiles of the same color
- completion of personal goals
- completion of common goals

It works fine, maybe a little tuning has to be done in order to improve the evaluation.

The only problem is the number of calculations it has to do:

if we assume a big board (4 players) then at worst we can have ca 300/400 moves  
to calculate by force (permutations included), and since for every move it checks also   
every possible column it could be inserted (at worst 5) we could arrive at 1500/2000 moves.

Now note that the algorithm checks for every cell the adjacent, so for every medium sized  
board we have to check 15 * 4= 60 cells per move. With a total of 90000/120000 operations done.

It is not that bad, but we can do much much better :)  

## Second Idea: NN

That is why i was thinking about 2. from the start, but i could not find an intelligent way  
to decide what to put in input (and consequentially in output).

Then some days ago i think i found a very pretty solution:

to understand the idea we have to ask ourselves: what has to see the neural network?  
Well, for sure the current gameStateRepresentation of the personal shelf, and at least all the possible moves in the position  
So... why not put them in the input then ? (for personal and common goals we need to do a deeper think)  
But, how can we encode the information of the move (YELLOW,YELLOW, BLUE) and all its permutations?  
I mean, how many different moves could there possibly even be out there?  
Well, for the moves with one tile we have 6 total, for two 6 * 6 and for three 6 * 6 * 6.

That is only a total of 258 possible moves, not many.  
So if we combine 258 + 30 (size of the shelf) we have only 288 input nodes.

And for the outputs? Well in output we have to choose between one of the 258 moves.

So we have a neural network with:

- 288 inputs
- 258 outputs

Which amounts to 74304 connections without any hidden layer.

But do we need hidden layers? For the moment i will say no,  
the explaination is pretty simple: we want a direct correlation between our input and output.

We do not want RNN (recursive neural networks), even if they are very appealing ;),  
or any hidden information that we have to compute. 

We just want a function that correlates to a position and a set of moves the best possible one.

## Update on NN

I completely forgot at first that i have to choose the column too,  
but i think i found a neat solution.

Instead of just a NN, i will have... two of them!!

The first one will be with 30 inputs (one for cell) and 5 outputs (one for column)  and it will help the NN learn where is more likely to insert a piece based on its current column.

The second one will be the 288 * 258 (or also 258 * 258, have to do some training and testing).


To also learn the common and personal goals, we have 12 of each.  
So we could do two mappings and insert them into the NN input.

But for understanding in which NN they should go we need to think a bit.  

If we have them in the first one, it means that the computer will learn to prefer  
a column based on the current gameStateRepresentation of the library and the objectives.

On the other hand, in the second one it will learn which move is best based on  
the same properties.

So if we put them in the first only, it will lose the fact that each position is  
evaluated by the common and personal goals too.  

But if we have it in the second only, it will lose the ability to evaluate the best column.  

And if we have them in both, we have a lot of redundancy (apart from more hidden nodes).

Soo... why not combine them both at this point?

We could have a NN with 312 (=258+30+12+12) inputs and 263 (=258+5) outputs.  
Where of course every output has two numbers which have to be chosen from.

This would only increase the number of hidden nodes to 82056.  
Which is not bad given the maths below.

## Some boring (and possibly wrong) MATHS

Computationally speaking, we could argue that there still have to be many operations to do.

But just trust me and listen, since in a standard NN we want to compute the output   
only if we have an input, at worst we would have to do the dot product  
between the inputs and the intermidiate weights for every output.

And in a normal game with 300/400 moves possible we have to consider each different move.  
But how many are there really?  

Now some probability.

If we do a little preditcions, assuming a randomly distributed board: (i.e. each set of moves  
has an equal chance of appearing), we could say that each move in the board could be  
represented as a subset of a poisson process with lambda = 1/6. So for every move we could say:

- single tile: every move covered

This one is easy, the probability of it not happening is just negligible

- double tile: 

Now for double tiles we have to calculate which is the probability of collsion   
between two tiles. They are in fact the same move if T1=(x1,x2) and T2=(y1,y2)  
are part of the same permutation of tiles, so if x1==y1||y2 and x2==y2||y1 (and vice versa)  
also each tile is equal to another one  with a probability of 1/36.  
So in the end we have a 16(=4*4) * (1/36) * (1/36) (=16/1296=1/81) of possibility that they are the same.

- triple tile: 

For triple tiles we could also do a similar reasoning, and we would find a 64/(216 * 216) (=1/729)

And with adjacent moves we have more chance that they are the same (have the same permutation),  
so we could do a better approximation with math but i will not bother, because the chances would   
rise much higher since now adjacent moves have more probability of being equal.

All of this maths to say that not every possible move has to be considerated at once, we just need  
to find a good estimate in order to try our model. I think that a bit more than half of the moves 
could be tested at once, with an activation rate of:

- single tile: 99.5% (sometimes its also good to leave some moves not possible)
- double tile: 92.5%
- triple tile: 30-40% (from 50-60%)

So for every operation we have to calculate at worst 70% of the total moves (approx 180).

So every connection active for the feed forward would be 180 * 258 = 46440.

Why did i lower the ones with triple tiles?  
Well, because in a normal game most of the tiles you can choose from are  
either single or double tiles.

And even if the triples carry the bigger number of permutations,  
they are more likely to repeat.

## Little bit of Backpropagation

Now not bad, but we also have to count the part of the backpropagation.

For only one layer of connections it is relatively straightforward, and the formula i use is this:

error=output-expected;

weight=weight + function(error) * weight * learningRate;

Where the function is the derivative of the sigmoid(the activation function used in the NN)  
and the learningRate for the moment is set at 0.1 for the moment.

## TODO

The only thing we miss is a framework to train the NN.

Now since simulating all the game is too complicated, we avoid it and this is what i came up with:

we have to simulate the function that reads the gameboard and sets all the possible moves  
we prepare the expected output based on the 1. method, so we have an automatic evaluation function  
we feed forward and backpropagate a loooooot of times and we bing chilling.

preferably we also want to save every k generations a json with the current configuration too.

Note that to generate the moves we have to pick a random move and then  
compute also all its different permutations (to make it more realistic)

## Ideas for Future

Some possible ideas in the future may be: 

- add a bias so that some neurons when activated weigh more

- add the recognition of personal objectives and common objectives (bit complicated)



