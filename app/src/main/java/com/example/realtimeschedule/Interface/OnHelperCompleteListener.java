package com.example.realtimeschedule.Interface;

import java.util.Map;

/**
 * Invoked when booking helper hs finished executing
 */
public interface OnHelperCompleteListener {
    // task completed successfully
    public void onSuccess(String message);
    // task completed but with an error
    public void onError(String errorMessage);
}
