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

package net.duckling.ddl.service.devent.listener;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import net.duckling.ddl.service.devent.DAction;
import net.duckling.ddl.service.devent.DEvent;

/**
 * @date 2011-11-2
 * @author clive
 */
@Service
public class PageEventListener extends AbstractEventListener {
	@PostConstruct
	public void init(){
		registQueue(PAGE_EVENT);
	}
	
	protected void addDifferenceNoticeToHandle(DEvent e){
		String operation = e.getEventBody().getOperation();
		if(DAction.CREATE.equals(operation)){
			create(e);
		}else if(DAction.DELETE.equals(operation)){
			delete(e);
		}else if(DAction.MODIFY.equals(operation)){
			modify(e);
		}else if(DAction.RECOMMEND.equals(operation)){
			recommend(e);
		}else if(DAction.RENAME.equals(operation)){
			rename(e);
		}else if(DAction.COMMENT.equals(operation)){
			comment(e);
		}else if(DAction.REPLY.equals(operation)){
			reply(e);
		}else if(DAction.RECOVER.equals(operation)){
			recover(e);
		}else if(DAction.MENTION.equals(operation)){
			mention(e);
		}else if(DAction.TEAM_MOVE.equals(operation)){
			teamMove(e);
		}
	}
	
	private void teamMove(DEvent e){
		appendFollowNotice(e);
		appendHistoryNotice(e);
		appendTeamNotice(e);
	}
	private void mention(DEvent e){
		appendMentionNotice(e);
	}
	private void recover(DEvent e){
		appendFollowNotice(e);
		appendConcernNotice(e);
		appendHistoryNotice(e);
		appendTeamNotice(e);
		appendOwnerNotice(e);
	}
	private void create(DEvent e){
		appendFollowNotice(e);
		appendHistoryNotice(e);
		appendTeamNotice(e);
	}
	
	private void modify(DEvent e){
		appendFollowNotice(e);
		appendConcernNotice(e);
		appendHistoryNotice(e);
		appendTeamNotice(e);
		appendOwnerNotice(e);
	}
	
	private void rename(DEvent e){
		appendConcernNotice(e);
		appendHistoryNotice(e);
		appendTeamNotice(e);
	}
	
	private void delete(DEvent e){
		appendFollowNotice(e);
		appendConcernNotice(e);
		appendHistoryNotice(e);
		appendTeamNotice(e);
		appendOwnerNotice(e);
	}
	
	private void recommend(DEvent e){
		appendRecommendNotice(e);
		appendHistoryNotice(e);
	}
	
	private void comment(DEvent e){
		appendOwnerNotice(e);
		appendCommentNotice(e);
		appendHistoryNotice(e);
		appendTeamNotice(e);
	}
	
	private void reply(DEvent e){
		appendReplyNotice(e);
		appendHistoryNotice(e);
//		appendTeamNotice(e);
	}
	

}
