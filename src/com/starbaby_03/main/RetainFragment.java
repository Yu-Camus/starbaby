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
 *һ���򵥵ķ�uiƬ�δ洢��������ͱ����������ø����ڱ������������ڱ���ImageCache����
 */
public class RetainFragment extends Fragment {
    private static final String TAG = "RetainFragment";
    private Object mObject;

    /**
     * �չ��캯������Ƭ���ļ�
     */
    public RetainFragment() {}

    /**
     * ����λ����ʵ����Ƭ�λ����û���ҵ�,�����������ʹ��FragmentManager��
     *
     * @param ��Ƶ��FragmentManager����ʹ��
     * @return The existing instance of the Fragment or the new instance if just
     *         created.
     */
    public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
        // ���������б������˵�Ƭ�Ρ�
        RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG);

        // ���������(���һ������),������Ҫ�������������
        if (mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            fm.beginTransaction().add(mRetainFragment, TAG).commit();
        }

        return mRetainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ȷ�����Ƭ�α����������øı�
        setRetainInstance(true);
    }

    /**
     * �洢һ�����������Ƭ�Ρ�
     *
     * @param object The object to store
     */
    public void setObject(Object object) {
        mObject = object;
    }

    /**
     * ��ô洢����
     *
     * @return The stored object
     */
    public Object getObject() {
        return mObject;
    }

}
