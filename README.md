# EagleEyes_Project
This is a simple Sign Recognition app that is built on OpenCvCameraView Class from android/OpenCV library. The App starts with a simple Main activity which leads to the CameraFrame Activity that performs the following:

Phase 1:
Scan the Environment 

![mobile1](https://cloud.githubusercontent.com/assets/7992156/25206385/8f003750-251c-11e7-9690-a541dec3dc75.png)

Search for rectangular objects or objects that have higher density of pixels.

![mobile2](https://cloud.githubusercontent.com/assets/7992156/25206386/8f031baa-251c-11e7-99be-a5746465e46d.png)

![mobile3](https://cloud.githubusercontent.com/assets/7992156/25206387/8f1344d0-251c-11e7-8917-ae15fbc17e86.png)

Use Hough Transform to highlight such areas with a distinct rectangle.

![smartyapp-detect](https://cloud.githubusercontent.com/assets/7992156/25206441/c616dc12-251c-11e7-91e7-a670696787a8.jpg)


Use Perspective Transform to get the image in an upright position for OCR.

Phase 2 (Which needs to be worked on):
Perform Noise Reduction on the cropped Image

Pass it through OCR.
