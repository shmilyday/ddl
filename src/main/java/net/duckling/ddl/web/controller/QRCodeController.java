/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */

package net.duckling.ddl.web.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.zxing.EncodeHintType;

/**
 * 生成二维码
 * @date 2015-5-27
 * @author Brett
 */
@Controller
@RequestMapping("/f/qrcode")
public class QRCodeController extends BaseController {
    private final static int DEFAULT_SIZE = 148;
	
	@RequestMapping
	public void display(@RequestParam("text")String text, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ByteArrayOutputStream out = QRCode.from(text).to(ImageType.PNG)
        		.withHint(EncodeHintType.MARGIN, 1).withSize(DEFAULT_SIZE, DEFAULT_SIZE).stream();
        resp.setContentType("image/png");
        resp.setContentLength(out.size());
        OutputStream outStream = resp.getOutputStream();
		outStream.write(out.toByteArray());
		outStream.flush();
        outStream.close();
	}
}
