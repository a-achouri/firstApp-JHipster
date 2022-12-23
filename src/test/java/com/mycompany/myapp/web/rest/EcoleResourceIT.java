package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ecole;
import com.mycompany.myapp.repository.EcoleRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EcoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EcoleResourceIT {

    private static final String DEFAULT_NOM_ECOLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ECOLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_ECOLE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_ECOLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TELEPHONE_ECOLE = 1;
    private static final Integer UPDATED_TELEPHONE_ECOLE = 2;

    private static final String ENTITY_API_URL = "/api/ecoles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EcoleRepository ecoleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEcoleMockMvc;

    private Ecole ecole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ecole createEntity(EntityManager em) {
        Ecole ecole = new Ecole().nomEcole(DEFAULT_NOM_ECOLE).adresseEcole(DEFAULT_ADRESSE_ECOLE).telephoneEcole(DEFAULT_TELEPHONE_ECOLE);
        return ecole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ecole createUpdatedEntity(EntityManager em) {
        Ecole ecole = new Ecole().nomEcole(UPDATED_NOM_ECOLE).adresseEcole(UPDATED_ADRESSE_ECOLE).telephoneEcole(UPDATED_TELEPHONE_ECOLE);
        return ecole;
    }

    @BeforeEach
    public void initTest() {
        ecole = createEntity(em);
    }

    @Test
    @Transactional
    void createEcole() throws Exception {
        int databaseSizeBeforeCreate = ecoleRepository.findAll().size();
        // Create the Ecole
        restEcoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecole)))
            .andExpect(status().isCreated());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeCreate + 1);
        Ecole testEcole = ecoleList.get(ecoleList.size() - 1);
        assertThat(testEcole.getNomEcole()).isEqualTo(DEFAULT_NOM_ECOLE);
        assertThat(testEcole.getAdresseEcole()).isEqualTo(DEFAULT_ADRESSE_ECOLE);
        assertThat(testEcole.getTelephoneEcole()).isEqualTo(DEFAULT_TELEPHONE_ECOLE);
    }

    @Test
    @Transactional
    void createEcoleWithExistingId() throws Exception {
        // Create the Ecole with an existing ID
        ecole.setId(1L);

        int databaseSizeBeforeCreate = ecoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEcoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecole)))
            .andExpect(status().isBadRequest());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEcoles() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        // Get all the ecoleList
        restEcoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ecole.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEcole").value(hasItem(DEFAULT_NOM_ECOLE)))
            .andExpect(jsonPath("$.[*].adresseEcole").value(hasItem(DEFAULT_ADRESSE_ECOLE)))
            .andExpect(jsonPath("$.[*].telephoneEcole").value(hasItem(DEFAULT_TELEPHONE_ECOLE)));
    }

    @Test
    @Transactional
    void getEcole() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        // Get the ecole
        restEcoleMockMvc
            .perform(get(ENTITY_API_URL_ID, ecole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ecole.getId().intValue()))
            .andExpect(jsonPath("$.nomEcole").value(DEFAULT_NOM_ECOLE))
            .andExpect(jsonPath("$.adresseEcole").value(DEFAULT_ADRESSE_ECOLE))
            .andExpect(jsonPath("$.telephoneEcole").value(DEFAULT_TELEPHONE_ECOLE));
    }

    @Test
    @Transactional
    void getNonExistingEcole() throws Exception {
        // Get the ecole
        restEcoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEcole() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();

        // Update the ecole
        Ecole updatedEcole = ecoleRepository.findById(ecole.getId()).get();
        // Disconnect from session so that the updates on updatedEcole are not directly saved in db
        em.detach(updatedEcole);
        updatedEcole.nomEcole(UPDATED_NOM_ECOLE).adresseEcole(UPDATED_ADRESSE_ECOLE).telephoneEcole(UPDATED_TELEPHONE_ECOLE);

        restEcoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEcole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEcole))
            )
            .andExpect(status().isOk());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
        Ecole testEcole = ecoleList.get(ecoleList.size() - 1);
        assertThat(testEcole.getNomEcole()).isEqualTo(UPDATED_NOM_ECOLE);
        assertThat(testEcole.getAdresseEcole()).isEqualTo(UPDATED_ADRESSE_ECOLE);
        assertThat(testEcole.getTelephoneEcole()).isEqualTo(UPDATED_TELEPHONE_ECOLE);
    }

    @Test
    @Transactional
    void putNonExistingEcole() throws Exception {
        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();
        ecole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEcoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ecole.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ecole))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEcole() throws Exception {
        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();
        ecole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ecole))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEcole() throws Exception {
        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();
        ecole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEcoleWithPatch() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();

        // Update the ecole using partial update
        Ecole partialUpdatedEcole = new Ecole();
        partialUpdatedEcole.setId(ecole.getId());

        partialUpdatedEcole.adresseEcole(UPDATED_ADRESSE_ECOLE);

        restEcoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEcole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEcole))
            )
            .andExpect(status().isOk());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
        Ecole testEcole = ecoleList.get(ecoleList.size() - 1);
        assertThat(testEcole.getNomEcole()).isEqualTo(DEFAULT_NOM_ECOLE);
        assertThat(testEcole.getAdresseEcole()).isEqualTo(UPDATED_ADRESSE_ECOLE);
        assertThat(testEcole.getTelephoneEcole()).isEqualTo(DEFAULT_TELEPHONE_ECOLE);
    }

    @Test
    @Transactional
    void fullUpdateEcoleWithPatch() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();

        // Update the ecole using partial update
        Ecole partialUpdatedEcole = new Ecole();
        partialUpdatedEcole.setId(ecole.getId());

        partialUpdatedEcole.nomEcole(UPDATED_NOM_ECOLE).adresseEcole(UPDATED_ADRESSE_ECOLE).telephoneEcole(UPDATED_TELEPHONE_ECOLE);

        restEcoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEcole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEcole))
            )
            .andExpect(status().isOk());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
        Ecole testEcole = ecoleList.get(ecoleList.size() - 1);
        assertThat(testEcole.getNomEcole()).isEqualTo(UPDATED_NOM_ECOLE);
        assertThat(testEcole.getAdresseEcole()).isEqualTo(UPDATED_ADRESSE_ECOLE);
        assertThat(testEcole.getTelephoneEcole()).isEqualTo(UPDATED_TELEPHONE_ECOLE);
    }

    @Test
    @Transactional
    void patchNonExistingEcole() throws Exception {
        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();
        ecole.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEcoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ecole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ecole))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEcole() throws Exception {
        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();
        ecole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ecole))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEcole() throws Exception {
        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();
        ecole.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ecole)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEcole() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        int databaseSizeBeforeDelete = ecoleRepository.findAll().size();

        // Delete the ecole
        restEcoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, ecole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
