package com.dragons0u1.utils;

import java.util.*;

import net.dv8tion.jda.api.entities.*;

public class TitleBuilder {

	public static String prepareTitle(List<Member> members) {
		String title = "";
		if (members.size() >= 1) {
			for (Member member : members) {
				if (members.size() == 1)
					title += member.getEffectiveName();
				else {
					if (members.indexOf(member) < members.size()-1)
						title += String.format("%s, ", member.getEffectiveName());
					else
						title += String.format("and %s", member.getEffectiveName());
				}
			}
		}
		return title;
	}
}
