# EagleEyes_Project
This is a simple Sign Recognition app that is built on OpenCvCameraView Class from android/OpenCV library. The App starts with a simple Main activity which leads to the CameraFrame Activity that performs the following:

Phase 1:
Scan the Environment 

Search for rectangular objects or objects that have higher density of pixels.

Use Hough Transform to highlight such areas with a distinct rectangle.

Use Perspective Transform to get the image in an upright position for OCR.

Phase 2 (Which needs to be worked on):
Perform Noise Reduction on the cropped Image

Pass it through OCR.
