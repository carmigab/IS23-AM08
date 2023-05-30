package it.polimi.ingsw.model;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * This class simulates a stack
 */
public class MyStack {
    /**
     * Attribute containing all the information of the stack
     */
    @Expose
    private final Stack<Integer> stack;

    /**
     * The constructor of the class creates an empty stack
     */
    public MyStack(){
        stack=new Stack<>();
    }

    /**
     * This method is the push function for a usual stack
     * @param i integer to push in the stack
     */
    public void push(Integer i){
        this.stack.add(i);
    }

    /**
     * This method is the push function for a usual stack
     * @return pop of the integer from the stack
     */
    public Integer pop(){
        return this.stack.remove(this.stack.size()-1);
    }

    /**
     * This method returns the top of the stack
     * @return the top of the stack
     */
    public Integer peek(){
        return this.stack.peek();
    }
}
