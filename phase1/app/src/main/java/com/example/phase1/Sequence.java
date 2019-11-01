package com.example.phase1;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Sequence {

    private static Random random = new Random();

  public Sequence() {
    random = new Random();
  }

  public static ArrayList<Integer> getSequence() {
    ArrayList<Integer> sequence = new ArrayList<Integer>();
    ArrayList<Integer> x = new ArrayList<>();
    x.add(0);
    x.add(1);
    x.add(2);
    x.add(3);
    for (int i = 0; i < 4; i++) {
      int item = x.get(ThreadLocalRandom.current().nextInt(x.size()));
      sequence.add(item);
      x.remove((Integer) item);
//      sequence.add(random.nextInt(i));
    }

    return sequence;
  }

  public static ArrayList<Integer> getSequence(int difficulty) {
    ArrayList<Integer> sequence = new ArrayList<Integer>();
    for (int i = 0; i < difficulty; i++) {
      sequence.add(random.nextInt(4));
    }

    return sequence;
  }
}
