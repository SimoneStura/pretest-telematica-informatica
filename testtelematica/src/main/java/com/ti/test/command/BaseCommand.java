package com.ti.test.command;

import com.ti.test.exception.InternalException;

public abstract class BaseCommand<OUTPUT> {

    public OUTPUT execute() throws InternalException {
        if (canExecute()) {
            return doExecute();
        } else {
            throw new InternalException(getExceptionMessage());
        }
    }

    protected abstract boolean canExecute();

    protected abstract OUTPUT doExecute() throws InternalException;

    protected abstract String getExceptionMessage();
}
