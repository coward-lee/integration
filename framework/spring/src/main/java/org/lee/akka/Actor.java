package org.lee.akka;

public class Actor {
    ComponentA componentA;
    ComponentB componentB;

    public Actor(ComponentA componentA, ComponentB componentB) {
        this.componentA = componentA;
        this.componentB = componentB;
    }

    public Actor() {
    }

    public void a(){
        componentA.a();
    }
    public void b(){
        componentB.b();
    }
}
