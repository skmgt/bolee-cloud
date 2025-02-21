package com.itheima.bolee.framework.outinterface.service;

import com.itheima.bolee.framework.outinterface.dto.TokenDTO;
import okhttp3.*;

import java.io.*;

public interface TokenService {


    TokenDTO getToken() throws IOException;
}
