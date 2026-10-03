package Vehicles {
    doc /* A small vehicle model used by every tutorial in this folder. */

    attribute def Length;
    attribute def Mass;
    attribute def Power;
    attribute def Volume;
    attribute def Speed;

    part def Wheel {
        attribute diameter : Length;
        attribute width : Length;
    }
    part def Engine {
        attribute power : Power;
        port fuelIn;
    }
    part def FuelTank {
        attribute capacity : Volume;
        port fuelOut;
    }

    part def Vehicle {
        doc /* The general vehicle: four wheels, one engine, one tank. */
        part wheels : Wheel[4];
        part engine : Engine;
        part tank : FuelTank;
        attribute mass : Mass;
        connect tank.fuelOut to engine.fuelIn;
    }

    part def SportsCar :> Vehicle {
        doc /* Inherits everything from Vehicle and adds a spoiler. */
        part spoiler;
        attribute topSpeed : Speed;
    }

    part myCar : SportsCar {
        attribute :>> mass = 1400;
    }
}
