package com.pegerto.ldaptest;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException {
		Hashtable<String, String> env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		
		Options options = new Options();
		options.addOption("h","help",false, "Get this help");
		options.addOption("s","server",true,"ldap server url");
		options.addOption("u","user",true,"auth user");
		options.addOption("p","password",true,"password");
		options.addOption("d","DN",true,"base DN");
		options.addOption("f","filter",true,"filter");
		
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse( options, args);
		if (args.length < 2){
			printHelp(options);
		}
		if (cmd.hasOption("h")){
			printHelp(options);
		}
		
		//server
		if (!cmd.hasOption("s")){
			System.out.println("Option server is mandatory.");
		} else {
			env.put(Context.PROVIDER_URL, cmd.getOptionValue("s"));
		}
		//user
		if (cmd.hasOption("u")){
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, cmd.getOptionValue("u"));
		}
		//passwd
		if (cmd.hasOption("p")){
			env.put(Context.SECURITY_CREDENTIALS, cmd.getOptionValue("p"));
		}
		
		
		try {
			DirContext ctx = new InitialDirContext(env);
			
			
			System.out.println("-----------------");
			System.out.println("connected");
			System.out.println("-----------------");

			if (cmd.hasOption("d") && cmd.hasOption("f")) {
				
				String dn = cmd.getOptionValue("d");
				String filter = cmd.getOptionValue("f");
				
				SearchControls ctls = new SearchControls();
				ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				ctls.setTimeLimit(30000);
				NamingEnumeration answer = ctx.search(dn, filter, ctls);

				while (answer.hasMore ()) {
					System.out.println("---------------------------");
		            SearchResult result = (SearchResult) answer.next ();    
		            Attributes attrs = result.getAttributes();
		            NamingEnumeration<String> ids = attrs.getIDs();
		            while(ids.hasMore()){
		            	String id = ids.next();
		            	System.out.println(attrs.get(id));
		            }
		            System.out.println("---------------------------");
				} 
				
			}
			
			ctx.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}

	public static void printHelp(Options options)
	{
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("ldaptest", options);
		System.exit(0);
	}
	
}
