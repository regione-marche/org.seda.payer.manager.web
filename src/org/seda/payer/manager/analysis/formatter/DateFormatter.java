//package org.seda.payer.manager.analysis.formatter;
//
//import java.text.SimpleDateFormat;
//
//import mondrian.olap.Member;
//import mondrian.olap.MemberFormatter;
//import mondrian.rolap.RolapMember;
//
//public class DateFormatter implements MemberFormatter {
//
//	private static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
//	
//	public String formatMember(Member member) {
//
//		return FORMATTER.format(((RolapMember) member).getKey());
//	}
//
//}
