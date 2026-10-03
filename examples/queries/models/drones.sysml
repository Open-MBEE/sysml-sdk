package Drones {
    doc /* A family of drones: a part tree with multiplicities given by attributes that each
         * drone fixes for itself, attribute values along the tree, connections and flows between
         * nested parts, and requirements satisfied by two drones.
         */
    private import ScalarValues::*;

    attribute def Power;
    attribute def Command;

    port def PowerPort { out attribute power : Power; }
    port def CommandPort { out attribute command : Command; }
    port def ShaftPort;

    part def Component {
        attribute mass : Real;
        attribute cost : Real;
        attribute totalMass : Real;
        attribute totalCost : Real;
    }

    part def Cell :> Component {
        attribute :>> mass = 0.045;
        attribute :>> cost = 4.0;
    }
    // A battery of any size: the drones below say how many cells theirs has.
    part def Battery :> Component {
        attribute :>> mass = 0.02;
        attribute :>> cost = 5.0;
        attribute cellCount : Natural;
        part cells : Cell[cellCount];
        port powerOut : PowerPort;
    }
    part def Motor :> Component {
        attribute :>> mass = 0.03;
        attribute :>> cost = 12.0;
        port powerIn : ~PowerPort;
        port commandIn : ~CommandPort;
        port shaft : ShaftPort;
    }
    part def Propeller :> Component {
        attribute :>> mass = 0.008;
        attribute :>> cost = 1.5;
        port hub : ~ShaftPort;
    }
    part def Rotor :> Component {
        attribute :>> mass = 0.002;
        attribute :>> cost = 0.5;
        part motor : Motor;
        part propeller : Propeller;
        connection drive connect [1] motor.shaft to [1] propeller.hub;
    }
    part def FlightController :> Component {
        attribute :>> mass = 0.015;
        attribute :>> cost = 30.0;
        port powerIn : ~PowerPort;
        port commandOut : CommandPort;
    }
    part def Frame :> Component {
        attribute :>> mass = 0.12;
        attribute :>> cost = 20.0;
    }
    part def PayloadBay :> Component {
        attribute :>> mass = 0.05;
        attribute :>> cost = 6.0;
    }

    // Any multicopter, with any number of rotors and any battery: each drone below fixes both.
    part def Multicopter :> Component {
        attribute :>> mass = 0.01;
        attribute :>> cost = 8.0;
        attribute rotorCount : Natural;

        part airframe : Frame;
        part battery : Battery;
        part controller : FlightController;
        part rotors : Rotor[rotorCount];

        connection supply connect [1] battery.powerOut to [1] controller.powerIn;
        flow powerFeed of Power from battery.powerOut.power to rotors.motor.powerIn.power;
        flow commands of Command from controller.commandOut.command to rotors.motor.commandIn.command;
    }

    part def Quadcopter :> Multicopter {
        attribute :>> rotorCount = 4;
        part :>> battery { attribute :>> cellCount = 6; }
    }
    part def Hexacopter :> Multicopter {
        attribute :>> rotorCount = 6;
        part :>> battery { attribute :>> cellCount = 6; }
    }
    part def CargoHexacopter :> Multicopter {
        attribute :>> rotorCount = 6;
        part :>> battery { attribute :>> cellCount = 8; }
        part payloadBay : PayloadBay;
    }

    // The required constraints are named: the SysML Toolkit does not yet pass unnamed members on
    // to specializations, and the requirement usages below reach these constraints by inheritance.
    requirement def MassLimit {
        doc /* The total mass of the subject does not exceed the limit. */
        subject vehicle : Component;
        attribute limit : Real;
        require constraint massBound { vehicle.totalMass <= limit }
    }
    requirement def CostLimit {
        doc /* The total cost of the subject stays within the budget. */
        subject vehicle : Component;
        attribute budget : Real;
        require constraint costBound { vehicle.totalCost <= budget }
    }

    requirement scoutMass : MassLimit { attribute :>> limit = 0.6; }
    requirement haulerMass : MassLimit { attribute :>> limit = 0.9; }
    requirement haulerCost : CostLimit { attribute :>> budget = 150.0; }

    part scout : Quadcopter;
    part hauler : CargoHexacopter;

    satisfy scoutMass by scout;
    satisfy haulerMass by hauler;
    satisfy haulerCost by hauler;
}
