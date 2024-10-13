package tn.esprit.tpfoyer.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @BeforeEach
    void setUp() {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllEtudiants() {
        // Préparer les données simulées
        Etudiant etudiant1 = new Etudiant(1L, "Ali", "Ben", 12345678, null, null);
        Etudiant etudiant2 = new Etudiant(2L, "Ahmed", "Salem", 87654321, null, null);
        List<Etudiant> mockEtudiants = Arrays.asList(etudiant1, etudiant2);

        // Définir le comportement du mock
        when(etudiantRepository.findAll()).thenReturn(mockEtudiants);

        // Appeler la méthode à tester
        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        // Vérifier les résultats
        Assert.assertEquals(2, result.size());
        assertEquals("Ali", result.get(0).getNomEtudiant());

        // Vérifier que la méthode findAll a bien été appelée
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    public void testRetrieveEtudiant() {
        // Préparer les données simulées
        Etudiant etudiant = new Etudiant(1L, "Ali", "Ben", 12345678, null, null);
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        // Appeler la méthode à tester
        Etudiant result = etudiantService.retrieveEtudiant(1L);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals("Ali", result.getNomEtudiant());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddEtudiant() {
        // Préparer les données simulées
        Etudiant etudiant = new Etudiant(1L, "Ali", "Ben", 12345678, null, null);
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        // Appeler la méthode à tester
        Etudiant result = etudiantService.addEtudiant(etudiant);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(1L, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testRemoveEtudiant() {
        // Appeler la méthode à tester
        etudiantService.removeEtudiant(1L);

        // Vérifier que la méthode deleteById a bien été appelée
        verify(etudiantRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testRecupererEtudiantParCin() {
        // Préparer les données simulées
        Etudiant etudiant = new Etudiant(1L, "Ali", "Ben", 12345678, null, null);
        when(etudiantRepository.findEtudiantByCinEtudiant(12345678L)).thenReturn(etudiant);

        // Appeler la méthode à tester
        Etudiant result = etudiantService.recupererEtudiantParCin(12345678L);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(12345678L, result.getCinEtudiant());
        verify(etudiantRepository, times(1)).findEtudiantByCinEtudiant(12345678L);
    }


}
