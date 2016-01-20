package com.iapps.ichat.helper;

import android.graphics.Bitmap;

public interface CameraPhotoListener
{
   public void setICPhotoTaken(Bitmap bmp, byte[] data);
   public void setUserPhotoTaken(Bitmap bmp, byte[] data);
}