package vehicle;

import org.junit.After;
import org.junit.Test;

import model.vehicle.Vehicle;
import model.vehicle.car.Volvo240;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.*;
import java.awt.geom.Point2D;

public class CarTestVolvo {

  Volvo240 vCar = new Volvo240();

  @After
  public void tearDown() {
    vCar = null;
  }

  @Test
  public void propertiesOfCarInitialised() {
    assertTrue(vCar.getNrDoors() > 0);
    assertTrue(vCar.getModelName().length() > 0);
    assertTrue(vCar.getColor() instanceof Color);
  }

  @Test
  public void doesTheCarStartFacingNorth() {
    assertEquals(new Point(0, 1), vCar.getDirection());
  }

  @Test
  public void moveShouldDoNothingIfEngineOFF() {
    Volvo240 unmovedCar = new Volvo240();
    vCar.move();
    assertEquals(unmovedCar.getPosition(), vCar.getPosition());
  }

  @Test
  public void startEngineShouldChangeCurrentSpeed() {
    Volvo240 unmovedCar = new Volvo240();
    vCar.startEngine();
    assertNotEquals(unmovedCar.getCurrentSpeed(), vCar.getCurrentSpeed());
  }

  @Test
  public void turningOffEngineShouldChangeCurrentSpeedToStationary() {
    Volvo240 unmovedCar = new Volvo240();
    vCar.startEngine();
    vCar.stopEngine();
    assertEquals(unmovedCar.getCurrentSpeed(), vCar.getCurrentSpeed(), 0.0001);
  }

  @Test
  public void turningShouldChangeDirection() {
    Volvo240 unturnedCar = new Volvo240();
    vCar.turnLeft();
    assertNotEquals(unturnedCar.getDirection(), vCar.getDirection());
  }

  @Test
  public void carTurningRightTwiceWillHaveTheSameDirectionAsIfItTurnedLeftTwice() {
    Volvo240 leftTurningCar = new Volvo240();
    vCar.turnRight();
    vCar.turnRight();
    leftTurningCar.turnLeft();
    leftTurningCar.turnLeft();
    assertEquals(leftTurningCar.getDirection(), vCar.getDirection());
  }

  @Test
  public void doesTurnLeftChangeDirectionBy90Degrees() {
    assertEquals(Vehicle.NORTH, vCar.getDirection());
    vCar.turnLeft();
    assertEquals(Vehicle.WEST, vCar.getDirection());
    vCar.turnLeft();
    assertEquals(Vehicle.SOUTH, vCar.getDirection());
    vCar.turnLeft();
    assertEquals(Vehicle.EAST, vCar.getDirection());
  }

  @Test
  public void doesTurnRightChangeDirectionBy90Degrees() {
    assertEquals(Vehicle.NORTH, vCar.getDirection());
    vCar.turnRight();
    assertEquals(Vehicle.EAST, vCar.getDirection());
    vCar.turnRight();
    assertEquals(Vehicle.SOUTH, vCar.getDirection());
    vCar.turnRight();
    assertEquals(Vehicle.WEST, vCar.getDirection());
  }

  @Test
  public void moveAfterTurningOnTheEngineDrivesNorth() {
    vCar.startEngine();
    assertEquals(new Point2D.Double(0.0, 0.0), vCar.getPosition());
    vCar.move();
    assertEquals(new Point2D.Double(0.0, 0.1), vCar.getPosition());
    vCar.move();
    assertEquals(new Point2D.Double(0.0, 0.2), vCar.getPosition());
  }

  @Test
  public void moveAfterTurningLeftDrivesWest() {
    vCar.startEngine();
    vCar.turnLeft();
    vCar.move();
    assertEquals(new Point2D.Double(-0.1, 0.0), vCar.getPosition());
  }

  @Test
  public void moveAfterTurningLeftDrivesEast() {
    vCar.startEngine();
    vCar.turnRight();
    vCar.move();
    assertEquals(new Point2D.Double(0.1, 0.0), vCar.getPosition());
  }

  @Test
  public void moveAfterMakingAUturnDrivesInOppositeDirection() {
    vCar.startEngine();
    assertEquals(Vehicle.NORTH, vCar.getDirection());
    vCar.turnLeft();
    vCar.turnLeft();
    vCar.move();
    assertEquals(new Point2D.Double(0.0, -0.1), vCar.getPosition());
  }

  @Test
  public void maximumBrakeReducesSpeedToZero() {
    vCar.startEngine();
    vCar.move();
    assertEquals(0.1, vCar.getCurrentSpeed(), 0.0001);
    vCar.brake(1);
    assertEquals(0.0, vCar.getCurrentSpeed(), 0.0001);
  }

  @Test
  public void OnePercentBrakeReducesSpeedButCarShouldStillBeMoving() {
    vCar.startEngine();
    assertEquals(0.1, vCar.getCurrentSpeed(), 0.000001);
    vCar.brake(0.01);
    assertEquals(0.0875, vCar.getCurrentSpeed(), 0.000001);
  }

  @Test
  public void halfGasShouldIncreaseSpeed() {
    vCar.startEngine();
    vCar.gas(0.5);
    assertEquals(0.725, vCar.getCurrentSpeed(), 0.0001);
  }

  @Test
  public void fullGasShouldUtiliseMaxEnginePower() {
    vCar.startEngine();
    vCar.gas(1);
    assertEquals(1.35, vCar.getCurrentSpeed(), 0.0001);
  }

  @Test
  public void fullGasWithMoveShouldRepetitvelyIncreasePositionQuickerInSameDirection() {
    vCar.startEngine();
    vCar.gas(1);
    vCar.move();
    assertEquals(new Point2D.Double(0.0, 1.35), vCar.getPosition());
    vCar.gas(1);
    vCar.move();
    assertEquals(new Point2D.Double(0.0, 3.95), vCar.getPosition());
    vCar.gas(1);
    vCar.move();
    assertEquals(new Point2D.Double(0.0, 7.8), vCar.getPosition());
  }

  @Test
  public void maxSpeedShouldNotBeMoreThanEnginePower() {
    double enginePower = vCar.getEnginePower();
    vCar.startEngine();
    while (true) {
      double currentSpeed = vCar.getCurrentSpeed();
      vCar.gas(1);
      double newSpeed = vCar.getCurrentSpeed();
      if (currentSpeed == newSpeed) {
        assertEquals(vCar.getCurrentSpeed(), enginePower, 0.0001);
        break;
      }
    }
  }

}