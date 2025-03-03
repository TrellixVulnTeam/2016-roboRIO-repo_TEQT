import numpy as np
import cv2

cap = cv2.VideoCapture(0)

fgbg = cv2.BackgroundSubtractorMOG()

while(1):
    ret, frame = cap.read()
   
    fgmask = fgbg.apply(frame)
   
    cv2.imshow('frame',fgmask)
    
    k = cv2.waitKey(0) & 0xff
    if k == 27:
        break

cap.release()
cv2.destroyAllWindows()