package com.glung.redux;

class BasicApplication {

    public static void main(String[] args) {
        final Store.Creator<Integer> creator = new Store.Creator<>();
        final ReduxApplication application = new ReduxApplication(creator, System.out);
        application.runDemo();
    }
}
