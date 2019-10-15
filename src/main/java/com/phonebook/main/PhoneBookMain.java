package com.phonebook.main;

import com.phonebook.spring.ApplicationConfig;
import com.phonebook.spring.PhoneBook;
import com.phonebook.spring.PhoneBookFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * PhoneBook entry point
 */
public class PhoneBookMain {

    public static void main(String[] args) {
        ApplicationContext context = newApplicationContext(args);

        Scanner sc = new Scanner(System.in);
        sc.useDelimiter(System.getProperty("line.separator"));

        PhoneBook phoneBook = context.getBean("phoneBook", PhoneBook.class);
        PhoneBookFormatter renderer = (PhoneBookFormatter) context.getBean("phoneBookFormatter");

        renderer.info("type 'exit' to quit.");
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.equals("exit")) {
                renderer.info("Have a good day...");
                break;
            }
            try {
                String[] lineArgs = line.split("\\s");
                switch (lineArgs[0]){
                    case "ADD":
                        String[] numbers = lineArgs[2].split(",");
                        for (int i = 0; i < numbers.length; i++){
                            phoneBook.addPhone(lineArgs[1], numbers[i].trim());
                        }
                        break;
                    case "REMOVE_PHONE":
                        phoneBook.removePhone(lineArgs[1]);
                        break;
                    case "SHOW":
                        renderer.show(phoneBook.findAll());
                        break;
                    default:
                        throw new UnsupportedOperationException("Operation not allowed");
                }
            } catch (Exception e) {
                renderer.error(e);
            }
        }
    }

    static ApplicationContext newApplicationContext(String... args) {
        return args.length > 0 && args[0].equals("classPath")
                ? new ClassPathXmlApplicationContext("application-config.xml")
                : new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }

}
