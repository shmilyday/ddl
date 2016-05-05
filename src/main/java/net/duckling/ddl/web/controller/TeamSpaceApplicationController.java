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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.ddl.common.VWBContext;
import net.duckling.ddl.common.VWBSession;
import net.duckling.ddl.constant.ELayout;
import net.duckling.ddl.service.resource.TeamSpaceSize;
import net.duckling.ddl.service.resource.TeamSpaceSizeService;
import net.duckling.ddl.service.team.Team;
import net.duckling.ddl.service.team.TeamService;
import net.duckling.ddl.service.team.TeamSpaceApplication;
import net.duckling.ddl.service.team.TeamSpaceApplicationService;
import net.duckling.ddl.service.url.UrlPatterns;
import net.duckling.ddl.util.FileSizeUtils;
import net.duckling.ddl.util.JsonUtil;
import net.duckling.ddl.web.AbstractSpaceController;
import net.duckling.ddl.web.interceptor.access.RequirePermission;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequirePermission(target = "team", operation = "admin")
@RequestMapping("/{teamCode}/spaceApplicetion")
public class TeamSpaceApplicationController extends AbstractSpaceController{
	@Autowired
	private TeamSpaceSizeService teamSpaceSizeService;
	@Autowired
	private TeamSpaceApplicationService teamSpaceApplicationService;
	@Autowired
	private TeamService teamService;
	@RequestMapping
	public ModelAndView display(HttpServletRequest request){
		int tid = VWBContext.getCurrentTid();
		TeamSpaceSize size = teamSpaceSizeService.getTeamSpaceSize(tid);
		boolean canApp = false;
		if(canApplicat(size)){
			canApp = true;
		}
		VWBContext context = VWBContext.createContext(request,UrlPatterns.ADMIN);
		Team t = teamService.getTeamByID(tid);
		ModelAndView mv = null;
		if(t.isPersonalTeam()){
			mv = layout(ELayout.LYNX_MAIN, context, "/jsp/aone/team/teamSpaceApplicationPersonal.jsp");
		}else{
			mv = layout(ELayout.LYNX_MAIN, context, "/jsp/aone/team/teamSpaceApplication.jsp");
		}
		List<TeamSpaceApplication> records = teamSpaceApplicationService.queryByTid(tid);
		List<Map<String,String>> res = new ArrayList<Map<String,String>>();
		long add = 0;
		for(TeamSpaceApplication re : records){
			Map<String,String> map = new HashMap<String,String>();
			map.put("type", re.getTypeDisplay());
			long s = re.getNewSize() - re.getOriginalSize();
			map.put("size", FileSizeUtils.getFileSize(s));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("time", sdf.format(re.getApproveTime()));
			add+=s;
			res.add(map);
		}
		mv.addObject("canApply", canApp);
		mv.addObject("size", size);
		mv.addObject("team", t);
		mv.addObject("records", res);
		mv.addObject("totalSize",FileSizeUtils.getFileSize(add));
		return mv;
	}

	public boolean canApplicat(TeamSpaceSize size) {
		return (size.getTotal()-size.getUsed())<=FileSizeUtils.ONE_GB*5;
	}

	@RequestMapping(params="func=manualApply")
	public void application(HttpServletRequest req,HttpServletResponse resp){
		JSONObject obj = new JSONObject();
		int tid = VWBContext.getCurrentTid();
		Team team = teamService.getTeamByID(tid);
		if(!team.isPersonalTeam()){
			obj.put("status", false);
			obj.put("message", "只有个人空间支持手动扩容!");
			JsonUtil.writeJSONObject(resp, obj);
			return;
		}
		TeamSpaceSize size = teamSpaceSizeService.getTeamSpaceSize(tid);
		if(!canApplicat(size)){
			obj.put("status", false);
			obj.put("message", "您的空间还未到扩容条件，请核对后再申请！");
			JsonUtil.writeJSONObject(resp, obj);
			return;
		}
		//10G
		long s = FileSizeUtils.ONE_GB*10L;
		long t = size.getTotal()/s;
		long newSize = (t+1)*s;
		String uid = VWBSession.getCurrentUid(req);
		teamSpaceApplicationService.add(newSize, size.getTotal(), uid, tid, TeamSpaceApplication.TYPE_MANUAL);
		obj.put("status", true);
		List<TeamSpaceApplication> records = teamSpaceApplicationService.queryByTid(tid);
		JSONArray arr = new JSONArray();
		long add = 0;
		for(TeamSpaceApplication re : records){
			JSONObject map = new JSONObject();
			map.put("type", re.getTypeDisplay());
			long ss = re.getNewSize() - re.getOriginalSize();
			map.put("size", FileSizeUtils.getFileSize(ss));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("time", sdf.format(re.getApproveTime()));
			add+=ss;
			arr.put(map);
		}
		size = teamSpaceSizeService.getTeamSpaceSize(tid);
		boolean canApp = false;
		if(canApplicat(size)){
			canApp = true;
		}
		putNewSize(obj, tid);
		obj.put("totalSize", FileSizeUtils.getFileSize(newSize));
		obj.put("canApp", canApp);
		obj.put("records", arr);
		obj.put("totalAddSize", add);
		JsonUtil.writeJSONObject(resp, obj);
	}
	
	private void putNewSize(JSONObject obj,int tid){
		TeamSpaceSize nowSize = teamSpaceSizeService.getTeamSpaceSize(tid);
		JSONObject size = new JSONObject();
		size.put("percent", nowSize.getPercentDisplay());
		size.put("total", nowSize.getTotalDisplay());
		size.put("used", nowSize.getUsedDisplay());
		obj.put("totalShow", size);
	}
	
}
