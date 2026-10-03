package Pumps {
    doc /* A pump controller whose behavior is a hierarchical state machine: composite states with
         * their own initial states, transitions that leave a composite state from any of its
         * substates, guards over attributes (one with a default value), and a state that nothing
         * leads to.
         */
    private import ScalarValues::*;

    attribute def PowerOn;
    attribute def PowerOff;
    attribute def SelfTestDone;
    attribute def Start;
    attribute def Stop;
    attribute def Boost;
    attribute def Relax;
    attribute def Overheat;
    attribute def Reset;

    part def PumpController {
        attribute temperature : Real;
        attribute selfTestPassed : Boolean;
        attribute maxTemperature : Real default 80.0;

        exhibit state modes {
            // The initial state: the one that follows the start of the state machine.
            first start then off;

            state off;
            state starting;
            state operating {
                first start then idle;

                state idle;
                state pumping {
                    first start then low;

                    state low;
                    state high;

                    transition low_to_high first low accept Boost then high;
                    transition high_to_low first high accept Relax then low;
                }

                transition idle_to_pumping first idle accept Start then pumping;
                transition pumping_to_idle first pumping accept Stop then idle;
            }
            state fault;
            state calibration;

            transition off_to_starting first off accept PowerOn then starting;
            transition starting_to_operating
                first starting
                accept SelfTestDone if selfTestPassed and temperature < maxTemperature
                then operating;
            transition starting_to_fault
                first starting
                accept SelfTestDone if not selfTestPassed
                then fault;
            transition operating_to_off first operating accept PowerOff then off;
            transition operating_to_fault first operating accept Overheat then fault;
            transition fault_to_off first fault accept Reset then off;
        }
    }
}
