/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer.text.tx3g;

import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.util.MimeTypes;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple Text parser that supports Tx3g presentation profile.
 * <p>
 * Supported features in this parser are:
 * <ul>
 *   <li>content
 *   <li>core
 *   <li>presentation
 *   <li>profile
 *   <li>structure
 *   <li>time-offset
 *   <li>timing
 *   <li>tickRate
 *   <li>time-clock-with-frames
 *   <li>time-clock
 *   <li>time-offset-with-frames
 *   <li>time-offset-with-ticks
 * </ul>
 * </p>
 * @see <a href="http://www.w3.org/TR/ttaf1-dfxp/">TTML specification</a>
 */
public class TextParser implements SubtitleParser {
  private static final String TAG = "TextParser";


  private final List<SubtitleData> mSubtitleList;

  /**
   * Equivalent to {@code TtmlParser(true)}.
   */
  public TextParser() {
    Log.i(TAG,"TextParser ");
    mSubtitleList = new LinkedList<SubtitleData>();
  }


  @Override
  public Subtitle parse(InputStream inputStream, String inputEncoding, long startTimeUs)
  throws IOException {

    DataInputStream in  = new DataInputStream(inputStream);
    String text = in.readUTF();
    text = (text == null) ? "" : text;
    Log.i(TAG,"parse(" + text + "," + startTimeUs + ")" );

    SubtitleData cue = new SubtitleData();
    cue.setSubtitleText(text);
    cue.setStartTimePos(startTimeUs);
    mSubtitleList.add(cue);

    Collections.sort(mSubtitleList, new Comparator<SubtitleData>() {
      @Override
      public int compare(SubtitleData o1 , SubtitleData o2) {
        if (o1.getStartTimePos() < o2.getStartTimePos())
          return -1;
        if (o1.getStartTimePos() > o2.getStartTimePos())
          return 1;
        return 0;
      }
    });
    TextSubtitle textSubtitle = new TextSubtitle(mSubtitleList);
    return textSubtitle;
  }

  @Override
  public boolean canParse(String mimeType) {
    boolean rtn = MimeTypes.TEXT_TX3G.equals(mimeType);
    Log.i(TAG,"canParse " + mimeType + "," + rtn);
    return rtn;
  }
}