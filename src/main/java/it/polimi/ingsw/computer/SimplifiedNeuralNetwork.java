package it.polimi.ingsw.computer;

import java.util.ArrayList;
import java.util.Random;

public class SimplifiedNeuralNetwork {

    private final ArrayList<Double> input;
    private final ArrayList<Double> output;
    private final ArrayList<ArrayList<ArrayList<Double>>> intermediateWeights;
    private final Random r;

    private final int INPUT_SIZE;
    private final int OUTPUT_SIZE;
    private final int NUM_HIDDEN_LAYERS;
    private final int SIZE_HIDDEN_LAYERS;

    public SimplifiedNeuralNetwork (int inputSize, int outputSize, int numHiddenLayers, int sizeHiddenLayers){
        numHiddenLayers++;
        this.input=new ArrayList<>(inputSize);
        for(int i=0; i<inputSize; i++){
            this.input.add(Double.valueOf(0));
        }
        this.output=new ArrayList<>(outputSize);
        for(int i=0; i<outputSize; i++){
            this.output.add(Double.valueOf(0));
        }
        this.intermediateWeights=new ArrayList<>(numHiddenLayers);
        this.intermediateWeights.add(0, new ArrayList<>(inputSize));
        for(int i=0;i<inputSize;i++){
            this.intermediateWeights.get(0).add(new ArrayList<>(sizeHiddenLayers));
            for(int j=0; j<sizeHiddenLayers; j++){
                this.intermediateWeights.get(0).get(i).add(Double.valueOf(0));
            }
        }
        if(numHiddenLayers>2){
            for(int i=1;i<numHiddenLayers-1;i++){
                this.intermediateWeights.add(new ArrayList<>(sizeHiddenLayers));
                for(int j=0; j<sizeHiddenLayers; j++){
                    this.intermediateWeights.get(i).add(new ArrayList<>(sizeHiddenLayers));
                    for(int k=0; k<sizeHiddenLayers; k++){
                        this.intermediateWeights.get(i).get(j).add(Double.valueOf(0));
                    }
                }
            }
        }
        this.intermediateWeights.add(this.intermediateWeights.size(), new ArrayList<>(outputSize));
        for(int i=0;i<outputSize;i++){
            this.intermediateWeights.get(this.intermediateWeights.size()-1).add(new ArrayList<>(sizeHiddenLayers));
            for(int j=0; j<sizeHiddenLayers; j++){
                this.intermediateWeights.get(this.intermediateWeights.size()-1).get(i).add(Double.valueOf(0));
            }
        }
        this.INPUT_SIZE=inputSize;
        this.OUTPUT_SIZE=outputSize;
        this.NUM_HIDDEN_LAYERS=numHiddenLayers;
        this.SIZE_HIDDEN_LAYERS=sizeHiddenLayers;
        r=new Random();
        this.randomizeHiddenLayers();
    }

    public void randomizeHiddenLayers(){
        for(ArrayList<ArrayList<Double>> num : this.intermediateWeights){
            for(ArrayList<Double> mun: num) {
                mun.clear();
                for (int i = 0; i < SIZE_HIDDEN_LAYERS; i++) mun.add(Double.valueOf(r.nextDouble() * 2 - 1));
            }
        }
    }

    public void setInput(ArrayList<Double> input){
        this.input.clear();
        this.input.addAll(input);
    }


    public void feedForward(){
        ArrayList<Double> partialResult=new ArrayList<>(SIZE_HIDDEN_LAYERS);
        ArrayList<Double> nextPartialResult=new ArrayList<>(SIZE_HIDDEN_LAYERS);

        ArrayList<Double> weightsForOneNode=new ArrayList<>(INPUT_SIZE);
        for(int i=0; i<SIZE_HIDDEN_LAYERS; i++){
            for(int j=0;j<INPUT_SIZE;j++) weightsForOneNode.add(this.intermediateWeights.get(0).get(j).get(i));
            partialResult.add(dotProduct(this.input, weightsForOneNode));
            weightsForOneNode.clear();
        }
        if(NUM_HIDDEN_LAYERS>2) {

            for (int i = 1; i < NUM_HIDDEN_LAYERS-1; i++) {
                for (int j = 0; j < SIZE_HIDDEN_LAYERS; j++) {
                    for (int k = 0; k < SIZE_HIDDEN_LAYERS; k++)
                        weightsForOneNode.add(this.intermediateWeights.get(i).get(k).get(j));
                    nextPartialResult.add(dotProduct(partialResult, weightsForOneNode));
                    weightsForOneNode.clear();
                }

                partialResult.clear();
                partialResult.addAll(nextPartialResult);
                nextPartialResult.clear();
            }

        }
        this.output.clear();
        for(int i=0; i<OUTPUT_SIZE; i++){
            for(int j=0;j<SIZE_HIDDEN_LAYERS;j++)
                weightsForOneNode.add(this.intermediateWeights.get(this.intermediateWeights.size()-1).get(i).get(j));
            this.output.add(dotProduct(partialResult, weightsForOneNode));
            weightsForOneNode.clear();
        }
    }

    private Double dotProduct(ArrayList<Double> a, ArrayList<Double> b){

        double sum=0;

        for(int i=0; i<a.size();i++) sum+=a.get(i)*b.get(i);

        return Double.valueOf(sum);
    }


    @Override
    public String toString() {

        String ret="Neural network:\n";

        ret+="Inputs:\n";
        for(Double d : this.input) ret+=d.toString()+"|";

        ret+="\n";

        ret+="Hidden:\n";

        for(ArrayList<ArrayList<Double>> num : this.intermediateWeights){
            for(ArrayList<Double> mun: num) {
                for (Double d : mun) ret+=d.toString()+"|";
                ret+="\n";
            }
            ret+="Next\n";
        }

        ret+="\n";

        ret+="Outputs:\n";
        for(Double d : this.output) ret+=d.toString()+"|";

        ret+="\n";

        return ret;
    }

    public String reducedToString(){
        String ret="Neural network:\n";

        ret+="Inputs:\n";
        for(Double d : this.input) ret+=d.toString()+"|";

        ret+="\n";

        ret+="Outputs:\n";
        for(Double d : this.output) ret+=d.toString()+"|";

        ret+="\n";

        return ret;
    }

}
