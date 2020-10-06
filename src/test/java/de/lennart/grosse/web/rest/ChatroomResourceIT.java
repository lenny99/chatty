package de.lennart.grosse.web.rest;

import de.lennart.grosse.ChattyApp;
import de.lennart.grosse.domain.Chatroom;
import de.lennart.grosse.repository.ChatroomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChatroomResource} REST controller.
 */
@SpringBootTest(classes = ChattyApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ChatroomResourceIT {

    private static final String DEFAULT_CHATROOM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHATROOM_NAME = "BBBBBBBBBB";

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Mock
    private ChatroomRepository chatroomRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatroomMockMvc;

    private Chatroom chatroom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chatroom createEntity(EntityManager em) {
        Chatroom chatroom = new Chatroom()
            .chatroomName(DEFAULT_CHATROOM_NAME);
        return chatroom;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chatroom createUpdatedEntity(EntityManager em) {
        Chatroom chatroom = new Chatroom()
            .chatroomName(UPDATED_CHATROOM_NAME);
        return chatroom;
    }

    @BeforeEach
    public void initTest() {
        chatroom = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatroom() throws Exception {
        int databaseSizeBeforeCreate = chatroomRepository.findAll().size();
        // Create the Chatroom
        restChatroomMockMvc.perform(post("/api/chatrooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isCreated());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeCreate + 1);
        Chatroom testChatroom = chatroomList.get(chatroomList.size() - 1);
        assertThat(testChatroom.getChatroomName()).isEqualTo(DEFAULT_CHATROOM_NAME);
    }

    @Test
    @Transactional
    public void createChatroomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatroomRepository.findAll().size();

        // Create the Chatroom with an existing ID
        chatroom.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatroomMockMvc.perform(post("/api/chatrooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isBadRequest());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllChatrooms() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get all the chatroomList
        restChatroomMockMvc.perform(get("/api/chatrooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].chatroomName").value(hasItem(DEFAULT_CHATROOM_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllChatroomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(chatroomRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChatroomMockMvc.perform(get("/api/chatrooms?eagerload=true"))
            .andExpect(status().isOk());

        verify(chatroomRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllChatroomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(chatroomRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChatroomMockMvc.perform(get("/api/chatrooms?eagerload=true"))
            .andExpect(status().isOk());

        verify(chatroomRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getChatroom() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        // Get the chatroom
        restChatroomMockMvc.perform(get("/api/chatrooms/{id}", chatroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatroom.getId().intValue()))
            .andExpect(jsonPath("$.chatroomName").value(DEFAULT_CHATROOM_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingChatroom() throws Exception {
        // Get the chatroom
        restChatroomMockMvc.perform(get("/api/chatrooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatroom() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        int databaseSizeBeforeUpdate = chatroomRepository.findAll().size();

        // Update the chatroom
        Chatroom updatedChatroom = chatroomRepository.findById(chatroom.getId()).get();
        // Disconnect from session so that the updates on updatedChatroom are not directly saved in db
        em.detach(updatedChatroom);
        updatedChatroom
            .chatroomName(UPDATED_CHATROOM_NAME);

        restChatroomMockMvc.perform(put("/api/chatrooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedChatroom)))
            .andExpect(status().isOk());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeUpdate);
        Chatroom testChatroom = chatroomList.get(chatroomList.size() - 1);
        assertThat(testChatroom.getChatroomName()).isEqualTo(UPDATED_CHATROOM_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingChatroom() throws Exception {
        int databaseSizeBeforeUpdate = chatroomRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatroomMockMvc.perform(put("/api/chatrooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(chatroom)))
            .andExpect(status().isBadRequest());

        // Validate the Chatroom in the database
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatroom() throws Exception {
        // Initialize the database
        chatroomRepository.saveAndFlush(chatroom);

        int databaseSizeBeforeDelete = chatroomRepository.findAll().size();

        // Delete the chatroom
        restChatroomMockMvc.perform(delete("/api/chatrooms/{id}", chatroom.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
