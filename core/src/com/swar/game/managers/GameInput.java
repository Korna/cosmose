package com.swar.game.managers;

/**
 * Created by Koma on 17.01.2017.
 */


    public class GameInput {
        public static int x;
        public static int y;
        public static boolean down;
        public static boolean pdown;
        public static boolean[] keys = new boolean[2];
        public static boolean[] pkeys = new boolean[2];
        private static final int NUM_KEYS = 2;
        public static final int BUTTON1 = 0;
        public static final int BUTTON2 = 1;


        public static void update() {
            pdown = down;

            for(int i = 0; i < 2; ++i) {
                pkeys[i] = keys[i];
            }

        }

        public static boolean isDown() {
            return down;
        }

        public static boolean isPressed() {
            return down && !pdown;
        }

        public static boolean isReleased() {
            return !down && pdown;
        }

        public static void setKey(int i, boolean b) {
            keys[i] = b;
        }

        public static boolean isDown(int i) {
            return keys[i];
        }

        public static boolean isPressed(int i) {
            return keys[i] && !pkeys[i];
        }
    }