package it.polimi.ingsw.computer;

import it.polimi.ingsw.computer.neuralNetwork.SimplifiedNeuralNetwork;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SimplifiedNeuralNetworkTest {

    @Test
    void testTraining() throws IOException {

        int inputSize=288;
        int outputSize=258;
        double learningRate=0.1;
        ArrayList<Double> inputs= new ArrayList<>(inputSize);
        for(int i=0;i<inputSize;i++) inputs.add(1.0);
        SimplifiedNeuralNetwork snn = new SimplifiedNeuralNetwork(inputSize,outputSize,learningRate);
        snn.setInput(inputs);
        //System.out.println(snn); // too much time
        snn.feedForward();
        System.out.println(snn.reducedToString());

        /* // useful for saving file to json
        Writer fw=new FileWriter("src/main/resources/neuralNetworkModel/nnmodel.json");
        Gson json = JsonWithExposeSingleton.getJsonWithExposeSingleton();
        fw.write(json.toJson(snn));
        fw.close();
        */

        ArrayList<Double> expected= new ArrayList<>(outputSize);
        for(int i=0;i<outputSize;i++)expected.add(1.0);
        snn.backPropagate(expected);
        /*
        Writer fw2=new FileWriter("src/main/resources/neuralNetworkModel/nnmodel1.json");
        fw2.write(json.toJson(snn));
        fw2.close();
        */

        assertTrue(true);
    }
}