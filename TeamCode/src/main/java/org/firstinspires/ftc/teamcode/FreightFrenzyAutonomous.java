/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Freight Frenzy game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "FreightFrenzyAutonomous", group = "Concept")
//@Disabled
public class FreightFrenzyAutonomous extends LinearOpMode {
  /* Note: This sample uses the all-objects Tensor Flow model (FreightFrenzy_BCDM.tflite), which contains
   * the following 4 detectable objects
   *  0: Ball,
   *  1: Cube,
   *  2: Duck,
   *  3: Marker (duck location tape marker)
   *
   *  Two additional model assets are available which only contain a subset of the objects:
   *  FreightFrenzy_BC.tflite  0: Ball,  1: Cube
   *  FreightFrenzy_DM.tflite  0: Duck,  1: Marker
   */

    //Test comment- 10/10/2021- 10:44 AM

    //Drive Motors- Vikrant
    DcMotor back_left;
    DcMotor back_right;
    DcMotor front_left;
    DcMotor front_right;

    //Carousel Spinning Motor- Vikrant
    //DcMotor carousel_spinner;

    //Arm to drop block on shipping hub
    //DcMotor arm;
    //Servo claw;



    //Variable for levels on shipping hub- Vikrant
    int level;

    private ElapsedTime runtime = new ElapsedTime();


    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
      "Ball",
      "Cube",
      "Duck",
      "Marker"
    };

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AenEeff/////AAABmVfM1TTQPUGRpC7PQZbNw25BExLdZ2MNuZfotDeDWeTSu/9GUq44dVnQHBGowhhEMplBNQLlvJ5Ai05PWcHybchVql4+VlfHGFu137Sc9dZgUEWSLYGmLcs4OG0HaX6qWsz9O5A0v/JbTJAMQHmF+DXSP0p1nIhiALIIU9jw7LnG+ik4k+xWAoFRdXVEzJMm8yVCjwZlowJjQd0hKRHXbJQG26T/rAEl4LwB9unLfnV6SsyEhUexEKPynizjgWMfqfbSlXUvBNqwURNXhBHmuGscNsbycdENESR89r0V1bZ0C/lOMs56VNfoi1G6+0u4JV9MF3gqaQx8xbzjE7B87ligZz87k5lpCnbbA/AfqZ6Q";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.

        //HardwareMap for Drive Motors and arm- Vikrant
        back_left = hardwareMap.dcMotor.get("back_left");
        back_right = hardwareMap.dcMotor.get("back_right");
        front_left = hardwareMap.dcMotor.get("front_left");
        front_right = hardwareMap.dcMotor.get("front_right");

        //arm = hardwareMap.get(DcMotor.class, "arm");


        back_left.setDirection(DcMotorSimple.Direction.REVERSE);
        front_left.setDirection(DcMotorSimple.Direction.REVERSE);

        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.0, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                //Move forward 2 inches- Vikrant
                //DriveforTime(0.5, 0.5, 0);

                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {

                            telemetry.addData("Entire Image Width", recognition.getImageWidth());
                            telemetry.addData("Entire Image Height", recognition.getImageHeight());
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            telemetry.addData("Image Height", recognition.getHeight());
                            telemetry.addData("Image Width", recognition.getWidth());

                            i++;


                        }
                    }
                      telemetry.update();
                      //telemetry.clearAll();

                      //if Statement to determine level for shipping hub and display level on driver station- Vikrant
                      if (updatedRecognitions != null) {

                          for (Recognition recognition : updatedRecognitions) {

                              double xCoordinate = recognition.getLeft();



                              if (recognition.getLabel() == "duck") {

                                  if (xCoordinate < 100) {

                                      level = 1;

                                      telemetry.addData("Level", level);


                                      telemetry.update();


                                  } else if (xCoordinate > 250 && xCoordinate < 400) {

                                      level = 2;
                                      telemetry.addData("Level", level);


                                      telemetry.update();



                                  } else if (updatedRecognitions == null) {

                                      level = 3;

                                      telemetry.addData("Level", level);


                                      telemetry.update();



                                  }


                              }


                          }

                      }

                      }




                }

                sleep(3000);
                //Drive to and spin the carousel- Vikrant
                StrafeLeftforTime(1, 4, 0);
                //SpinCarousel(1, 2.5, 0);

                //Go to shipping hub and drop block on correct level- Vikrant
                if(level == 1) {

                    StrafeRightforTime(1, 5.5, 0);
                    DriveforTime(0.5, 1, 0);
                    //dropBlockLevel1();

                }
                if(level == 2){

                    StrafeRightforTime(1, 5.5, 0);
                    DriveforTime(0.5, 1, 0);
                    //dropBlockLevel2();
                }
                if(level == 3){

                    StrafeRightforTime(1, 5.5, 0);
                    DriveforTime(0.5, 1, 0);
                    //dropBlockLevel3();
                }

                //Move back 2 inches- Vikrant
                DriveforTime(-1, 0.25, 0);

                //Turn right to park in warehouse- Vikrant
                turnRight(1, 2.5, 0);

                //Drive to warehouse and park inside warehouse- Vikrant
                DriveforTime(1,5.5,0);

            }
        }


    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
       tfodParameters.minResultConfidence = 0.8f;
       tfodParameters.isModelTensorFlow2 = true;
       tfodParameters.inputSize = 320;
       tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
       tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    //Function to make robot turn right

        public void turnRight(double speed,
        double seconds,
        double timeoutS) {

            // Ensure that the opmode is still active
            if (opModeIsActive()) {

                runtime.reset();
                back_right.setPower(0);
                back_left.setPower(speed);
                front_right.setPower(0);
                front_left.setPower(speed);
                seconds = seconds*1000;
                double i = 0;
                while (opModeIsActive() && i < seconds &&
                        (runtime.seconds() < timeoutS)) {
                    sleep(1);
                    i++;
                }

                // Stop all motion;
                front_right.setPower(0);
                back_right.setPower(0);
                front_left.setPower(0);
                back_left.setPower(0);

            }
        }


    //Drive forward for a certain number of seconds
    public void DriveforTime(double speed,
                             double seconds,
                             double timeoutS) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            runtime.reset();
            back_right.setPower(speed);
            back_left.setPower(speed);
            front_right.setPower(speed);
            front_left.setPower(speed);
            seconds = seconds*1000;
            double i = 0;
            while (opModeIsActive() && i < seconds &&
                    (runtime.seconds() < timeoutS)) {
                sleep(1);
                i++;
            }

            // Stop all motion;
            front_right.setPower(0);
            back_right.setPower(0);
            front_left.setPower(0);
            back_left.setPower(0);

        }
    }


    public void StrafeLeftforTime(double speed,
                             double seconds,
                             double timeoutS) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            runtime.reset();
            back_right.setPower(speed);
            back_left.setPower(-speed);
            front_right.setPower(speed);
            front_left.setPower(-speed);
            seconds = seconds*1000;
            double i = 0;
            while (opModeIsActive() && i < seconds &&
                    (runtime.seconds() < timeoutS)) {
                sleep(1);
                i++;
            }

            // Stop all motion;
            front_right.setPower(0);
            back_right.setPower(0);
            front_left.setPower(0);
            back_left.setPower(0);

        }
    }


    public void StrafeRightforTime(double speed,
                                  double seconds,
                                  double timeoutS) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            runtime.reset();
            back_right.setPower(-speed);
            back_left.setPower(speed);
            front_right.setPower(-speed);
            front_left.setPower(speed);
            seconds = seconds*1000;
            double i = 0;
            while (opModeIsActive() && i < seconds &&
                    (runtime.seconds() < timeoutS)) {
                sleep(1);
                i++;
            }

            // Stop all motion;
            front_right.setPower(0);
            back_right.setPower(0);
            front_left.setPower(0);
            back_left.setPower(0);

        }
    }
    /*
    public void SpinCarousel(double speed,
                                   double seconds,
                                   double timeoutS) {

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            runtime.reset();
            carousel_spinner.setPower(speed);

            seconds = seconds*1000;
            double i = 0;
            while (opModeIsActive() && i < seconds &&
                    (runtime.seconds() < timeoutS)) {
                sleep(1);
                i++;
            }

            // Stop all motion;
            carousel_spinner.setPower(0);


        }
    }
    */

    //Function to drop block on correct level- Vikrant
    /*
    public void dropBlockLevel1(){

        if(opModeIsActive()) {

            arm.setPower(0.5);
            moveArmDown(-70);
            clawOpen();
            clawClose();
            moveArmUp(0);

        }


    }

    public void dropBlockLevel2(){

        if(opModeIsActive()) {

            arm.setPower(0.5);
            moveArmDown(-50);
            clawOpen();
            clawClose();
            moveArmUp(0);

        }
    }

    public void dropBlockLevel3(){

        if(opModeIsActive()) {

            arm.setPower(0.5);
            moveArmDown(-30);
            clawOpen();
            clawClose();
            moveArmUp(0);
        }
    }

    //arm and claw control functions
    public void clawOpen() {
        clawControl(0);
    }
    public void clawClose() {
        clawControl(1);
    }
    public void clawControl(double position) {

        claw.setPosition(position);
        sleep(2000);

    }
    //Function that makes the arm move downwards
    public void moveArmDown(double angle) {

        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setPower(0.6);
        sleep(600);

        arm.setPower(0);




    }

    //Function that moves arm up
    public void moveArmUp(double angle) {
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setPower(-0.8);
        sleep(500);

        arm.setPower(0);



    }
*/
}
