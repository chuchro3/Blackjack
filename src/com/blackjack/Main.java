package com.blackjack;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    Scanner scan = new Scanner(System.in);
	    System.out.println("Hello there.");
	    System.out.print("Enter number of players (1-8): ");
	    int numPlayers = 0;
	    while (numPlayers < 1 || numPlayers > 8) {
			try {
				numPlayers = scan.nextInt();
			} catch(InputMismatchException e) {
				numPlayers = 0;
				scan = new Scanner(System.in);
			}
        }


		System.out.print("Include AI player? (y/n): ");
		String response = "";
		while (!response.equals("y") && !response.equals("n")) {
			response = scan.next();
		}
		boolean include_AI = false;
		if (response.equals("y")) {
			include_AI = true;
		}

        Thread blackjackThread = new Thread(new Blackjack(numPlayers, include_AI));
	    blackjackThread.start();
    }
}
