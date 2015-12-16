package com.home.intelligentsystems.cardreader;

import com.home.intelligentsystems.cardreader.Model.Employee;

/**
 * Created by intelligentsystems on 16/12/15.
 */
public interface TaskDelegate {
    void taskCompletionResult(Employee employee);
}