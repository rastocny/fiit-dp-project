/*
 * Copyright 2017 Andrej Mlyncar <a.mlyncar@gmail.com>.
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
package com.mlyncar.dp.analyzer.entity.impl;

import com.mlyncar.dp.analyzer.entity.Lifeline;

/**
 *
 * @author Andrej Mlyncar <a.mlyncar@gmail.com>
 */
public class LifelineImpl implements Lifeline {

    private String name;
    private String packageName;

    public LifelineImpl(String name) {
        this.name = name;
    }

    public LifelineImpl(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPackageName() {
        if (packageName == null) {
            return "";
        }
        return this.packageName;
    }
}
