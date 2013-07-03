/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.starbaby_03.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 *一个简单的非ui片段存储单个对象和被保留在配置更改在本例中它将用于保留ImageCache对象。
 */
public class RetainFragment extends Fragment {
    private static final String TAG = "RetainFragment";
    private Object mObject;

    /**
     * 空构造函数根据片段文件
     */
    public RetainFragment() {}

    /**
     * 　定位现有实例的片段或如果没有找到,创建和添加它使用FragmentManager。
     *
     * @param 调频的FragmentManager经理使用
     * @return The existing instance of the Fragment or the new instance if just
     *         created.
     */
    public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
        // 看看我们有保留工人的片段。
        RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG);

        // 如果不保留(或第一次运行),我们需要创建和添加它。
        if (mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            fm.beginTransaction().add(mRetainFragment, TAG).commit();
        }

        return mRetainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 确保这个片段被保留在配置改变
        setRetainInstance(true);
    }

    /**
     * 存储一个对象在这个片段。
     *
     * @param object The object to store
     */
    public void setObject(Object object) {
        mObject = object;
    }

    /**
     * 获得存储对象。
     *
     * @return The stored object
     */
    public Object getObject() {
        return mObject;
    }

}
