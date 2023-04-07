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
     * This method is the push function for an usual stack
     * @param i integer to push in the stack
     */
    public void push(Integer i){
        this.stack.add(i);
    }

    /**
     * This method is the push function for an usual stack
     * @return pop of the integer from the stack
     */
    public Integer pop(){
        return this.stack.remove(this.stack.size()-1);
    }

    /**
     * This method returns a copy of the stack of the common goal
     * @return a copy of the stack (in the form of list)
     */
    public List<Integer> getStackCopy(){
        return this.stack.stream().sorted().collect(Collectors.toList());
    }
}
