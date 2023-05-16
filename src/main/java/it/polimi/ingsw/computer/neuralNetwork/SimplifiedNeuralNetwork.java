package it.polimi.ingsw.computer.neuralNetwork;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Random;

public class SimplifiedNeuralNetwork {

    @Expose
    private final int INPUT_SIZE;
    @Expose
    private final int OUTPUT_SIZE;
    @Expose
    private final Double LEARNING_RATE;
    @Expose
    private final ArrayList<Double> input;
    @Expose
    private final ArrayList<Double> output;
    @Expose
    private final ArrayList<ArrayList<Double>> intermediateWeights;
    private final Random r;

    public SimplifiedNeuralNetwork (int inputSize, int outputSize, Double learningRate){
        this.INPUT_SIZE=inputSize;
        this.OUTPUT_SIZE=outputSize;
        this.LEARNING_RATE=learningRate;
        this.input=new ArrayList<>(inputSize);
        for(int i=0; i<inputSize; i++){
            this.input.add((double) 0);
        }
        this.output=new ArrayList<>(outputSize);
        for(int i=0; i<outputSize; i++){
            this.output.add((double) 0);
        }
        this.intermediateWeights=new ArrayList<>(this.OUTPUT_SIZE);
        for(int i=0;i<this.OUTPUT_SIZE;i++){
            this.intermediateWeights.add(new ArrayList<>(this.INPUT_SIZE));
            for(int j=0; j<this.INPUT_SIZE; j++){
                this.intermediateWeights.get(i).add((double) 0);
            }
        }
        r=new Random();
        this.randomizeHiddenLayer();
    }

    public void randomizeHiddenLayer(){
        for(ArrayList<Double> num : this.intermediateWeights){
            num.clear();
            for (int i = 0; i < this.INPUT_SIZE; i++) num.add(r.nextDouble() * 2 - 1);
        }
    }

    public void setInput(ArrayList<Double> input){
        this.input.clear();
        this.input.addAll(input);
    }


    public void feedForward(){
        this.output.clear();
        for(int i=0; i<this.OUTPUT_SIZE;i++){
            this.output.add(i, this.evaluateFunctionSigmoid(this.dotProduct(this.input, this.intermediateWeights.get(i))));
        }
    }

    public void backPropagate(ArrayList<Double> expected){
        ArrayList<Double> errors=new ArrayList<>(this.OUTPUT_SIZE);
        for(int i=0;i<this.OUTPUT_SIZE;i++) errors.add(i, this.output.get(i)-expected.get(i)); //calculate all errors
        for(int i=0;i<this.OUTPUT_SIZE;i++) { //for every output weight
            for(int j=0;j<this.INPUT_SIZE;j++){
                if(this.input.get(j)!=0){ //modify the weight only if the node was activated
                    //we have to modify the current weight based on how much it impacted the bad/good guess
                    //so we add to the current weight the error multiplied by the derivative of the function (we get an error delta) and we weigh it by this weight's weight
                    this.intermediateWeights.get(i).set(j, this.intermediateWeights.get(i).get(j)+this.evaluateFunctionSigmoidDerivative(errors.get(i))*this.intermediateWeights.get(i).get(j)*this.LEARNING_RATE);
                }
            }
        }
    }

    private Double dotProduct(ArrayList<Double> a, ArrayList<Double> b){

        double sum=0;

        for(int i=0; i<a.size();i++) sum+=a.get(i)*b.get(i);

        return sum;
    }

    private Double evaluateFunctionSigmoid(Double d){
        return 1/(1+Math.exp(-d));
    }

    private Double evaluateFunctionSigmoidDerivative(Double d){
        return d*(1-d);
    }


    @Override
    public String toString() {

        String ret="Neural network:\n";

        ret+="Inputs:\n";
        for(Double d : this.input) ret+=d.toString()+"|";

        ret+="\n";

        ret+="Hidden:\n";

        for(ArrayList<Double> arr: this.intermediateWeights) {
            for (Double d : arr) ret+=d.toString()+"|";
            ret+="\n";
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
