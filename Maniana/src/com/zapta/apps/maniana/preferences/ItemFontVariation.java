/*
 * Copyright (C) 2011 The original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */


package com.zapta.apps.maniana.preferences;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

import com.zapta.apps.maniana.main.AppContext;

/**
 * Represents parameters of selected font for items text.
 * 
 * Instances of this class cache the various parameters needed to set the currently selected item
 * font. This class is immutable.
 * 
 * @author Tal Dayan
 */
public class ItemFontVariation {
    /** Path to page title fonts relative to assets directory. */
    private static final String CURSIVE_ITEM_FONT_ASSET_PATH = "fonts/Vavont/Vavont.ttf";

    private static final String ELEGANT_ITEM_FONT_ASSET_PATH = "fonts/Pompiere/Pompiere-Regular.ttf";
    
//    private static final String LUCID_ITEM_FONT_ASSET_PATH = "fonts/Questrial/Questrial-Regular.ttf";
    
    private final Typeface mTypeFace;
    private final int mColor;
    private final int mColorCompleted;
    private final int mTextSize;
    private final float mLineSpacingMultiplier;
    private final int mTopBottomPadding;

    /**
     * Construct a new variation.
     * 
     * @param typeFace the typeface t use
     * @param color the text color for non completed items.
     * @param colorCompleted the text color for completed items.
     * @param textSize the text size.
     * @param lineSpacingMultiplier The line spacing multiplier to use.
     * @param topBottomPadding padding (in dip) at top and bottom of text.
     */
    public ItemFontVariation(Typeface typeFace, int color, int colorCompleted, int textSize,
                    float lineSpacingMultiplier, int topBottomPadding) {
        this.mTypeFace = typeFace;
        this.mColor = color;
        this.mColorCompleted = colorCompleted;
        this.mTextSize = textSize;
        this.mTopBottomPadding = topBottomPadding;
        this.mLineSpacingMultiplier = lineSpacingMultiplier;
    }

    /**
     * Apply this font variation to given text view.
     * 
     * @param textView the item's text view.
     * @param isCompleted true if the item is completed.
     */
    public void apply(TextView textView, boolean isCompleted, boolean applyAlsoColor) {
        textView.setTypeface(mTypeFace);
        if (applyAlsoColor) {
            textView.setTextColor(isCompleted ? mColorCompleted : mColor);
        }
        textView.setTextSize(mTextSize);
        textView.setLineSpacing(0.0f, mLineSpacingMultiplier);
        textView.setPadding(textView.getPaddingLeft(), mTopBottomPadding, textView
                        .getPaddingRight(), mTopBottomPadding);

        if (isCompleted) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
    
    public static final ItemFontVariation newFromCurrentPreferences(AppContext app) {
        final FontType fontType = app.pref().getItemFontTypePreference();
        // TODO: move these consts to XML file (can we use styles for this?)
        final int Color = app.pref().getPageItemActiveTextColorPreference();
        final int completedColor = app.pref().getPageItemCompletedTextColorPreference();

        final FontSize fontSize = app.pref().getItemFontSizePreference();
        final float k = fontSize.getFactor(); 
        
        switch (fontType) {
            case CURSIVE:
                return new ItemFontVariation(Typeface.createFromAsset(app.context()
                                .getAssets(), CURSIVE_ITEM_FONT_ASSET_PATH), Color,
                                completedColor, (int) (22 * k), 0.9f, 10);
            case ELEGANT:
                return new ItemFontVariation(Typeface.createFromAsset(app.context()
                                .getAssets(), ELEGANT_ITEM_FONT_ASSET_PATH), Color,
                                completedColor, (int) (24 * k), 1.0f, 10);
//            case LUCID:
//                return new ItemFontVariation(Typeface.createFromAsset(app.context()
//                                .getAssets(), LUCID_ITEM_FONT_ASSET_PATH), Color,
//                                completedColor, (int) (20 * k), 1.0f, 10);
            case SAN_SERIF:
                return new ItemFontVariation(Typeface.SANS_SERIF, Color,
                                completedColor, (int) (18 * k), 1.1f, 10);
            case SERIF:
                return new ItemFontVariation(Typeface.SERIF, Color,
                                completedColor, (int) (18 * k), 1.1f, 10);
            default:
                throw new RuntimeException("Unknown font type: " + fontType);
        }
    }
}
