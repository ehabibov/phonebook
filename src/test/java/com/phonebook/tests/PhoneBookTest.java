package com.phonebook.tests;

import com.phonebook.spring.ApplicationConfig;
import com.phonebook.spring.PhoneBook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
public class PhoneBookTest {

    @Autowired
    private PhoneBook phoneBook;

    @Test
    public void get_person_phone_numbers() {
        final Set<String> expected = new HashSet<>(asList("+79601232233"));
        assertEquals("phone numbers do not match",
                expected,
                phoneBook.findAll().get("Alex"));
    }

    @Test
    public void add_phone_to_new_person_if_phone_not_exist() {
        String name = "Jack";
        String telNo = "+13975554401";
        assertNull(phoneBook.findAllPhonesByName(name));
        assertFalse(phoneBook.findAll().containsKey(name));
        phoneBook.addPhone(name, telNo);
        assertTrue(phoneBook.findAll().containsKey(name));
        assertTrue(phoneBook.findAllPhonesByName(name).contains(telNo));
    }

    @Test
    public void add_phone_to_existing_person_if_phone_not_exist() {
        String name = "Rob";
        String telNo1 = "+13975554501";
        String telNo2 = "+13975554502";
        assertFalse(phoneBook.findAll().containsKey(name));
        assertNull(phoneBook.findAllPhonesByName(name));
        phoneBook.addPhone(name, telNo1);
        phoneBook.addPhone(name, telNo2);
        assertTrue(phoneBook.findAllPhonesByName(name).contains(telNo1));
        assertTrue(phoneBook.findAllPhonesByName(name).contains(telNo2));

    }

    @Test
    public void existing_phone_removed() {
        String name = "Alice";
        String telNo1 = "+13975554601";
        String telNo2 = "+13975554602";
        assertFalse(phoneBook.findAll().containsKey(name));
        assertNull(phoneBook.findAllPhonesByName(name));
        phoneBook.addPhone(name, telNo1);
        phoneBook.addPhone(name, telNo2);
        assertTrue(phoneBook.findAllPhonesByName(name).contains(telNo1));
        assertTrue(phoneBook.findAllPhonesByName(name).contains(telNo2));
        phoneBook.removePhone(telNo1);
        assertFalse(phoneBook.findAllPhonesByName(name).contains(telNo1));
        assertTrue(phoneBook.findAllPhonesByName(name).contains(telNo2));
    }

    @Test
    public void last_existing_phone_removed_with_person() {
        String name = "Andy";
        String telNo1 = "+13975554701";
        assertFalse(phoneBook.findAll().containsKey(name));
        assertNull(phoneBook.findAllPhonesByName(name));
        phoneBook.addPhone(name, telNo1);
        assertTrue(phoneBook.findAllPhonesByName(name).contains(telNo1));
        phoneBook.removePhone(telNo1);
        assertNull(phoneBook.findAllPhonesByName(name));
        assertFalse(phoneBook.findAll().containsKey(name));
    }

    @Test(expected = IllegalArgumentException.class)
    public void not_existing_phone_removal_throws_exception() {
        String name = "Mira";
        String telNo1 = "+13975554801";
        String telNo2 = "+13975554802";
        assertFalse(phoneBook.findAll().containsKey(name));
        assertNull(phoneBook.findAllPhonesByName(name));
        phoneBook.addPhone(name, telNo1);
        assertTrue(phoneBook.findAllPhonesByName(name).contains(telNo1));
        phoneBook.removePhone(telNo2);
        assertNull(phoneBook.findAllPhonesByName(name));
    }
}
