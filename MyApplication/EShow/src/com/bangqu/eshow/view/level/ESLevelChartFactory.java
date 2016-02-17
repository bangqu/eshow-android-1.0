/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bangqu.eshow.view.level;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AbLevelChart objects.
 */
public class ESLevelChartFactory {


  /**
   * Instantiates a new ab level chart factory.
   */
  private ESLevelChartFactory() {
  }


  /**
   * Gets the level chart view.
   *
   * @param context the context
   * @param dataset the dataset
   * @param renderer the renderer
   * @return the level chart view
   */
  public static final ESLevelView getLevelChartView(Context context,
      ESLevelSeriesDataset dataset, ESLevelSeriesRenderer renderer) {
    checkParameters(dataset, renderer); 
    ESLevelChart chart = new ESLevelChart(dataset, renderer);
    return new ESLevelView(context, chart);
  }

  
  /**
   * Check parameters.
   *
   * @param dataset the dataset
   * @param renderer the renderer
   */
  private static void checkParameters(ESLevelSeriesDataset dataset,ESLevelSeriesRenderer renderer) {
    if (dataset == null || renderer == null) {
      throw new IllegalArgumentException(
          "Dataset and renderer should be not null");
    }
  }

}
