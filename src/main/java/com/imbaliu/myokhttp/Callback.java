package com.imbaliu.myokhttp;

import java.io.IOException;

public interface Callback {

    void onFailure(Call call, IOException exception);

    void onResponse(Call call ,Response response);


}
