package dev.squad04.projetoFlap.board.service;

import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.board.dto.setor.AssociarUsuarioSetorDTO;
import dev.squad04.projetoFlap.board.dto.setor.CriarSetorDTO;
import dev.squad04.projetoFlap.board.entity.Anexo;
import dev.squad04.projetoFlap.board.entity.Setor;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetor;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetorId;
import dev.squad04.projetoFlap.board.repository.AnexoRepository;
import dev.squad04.projetoFlap.board.repository.SetorRepository;
import dev.squad04.projetoFlap.board.repository.UsuarioSetorRepository;
import dev.squad04.projetoFlap.exceptions.AppException;
import dev.squad04.projetoFlap.file.service.FileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SetorService {

    private final SetorRepository setorRepository;
    private final UserRepository userRepository;
    private final UsuarioSetorRepository usuarioSetorRepository;
    private final FileStorageService fileStorageService;
    private final AnexoRepository anexoRepository;

    public SetorService(SetorRepository setorRepository, UserRepository userRepository, UsuarioSetorRepository usuarioSetorRepository, FileStorageService fileStorageService, AnexoRepository anexoRepository) {
        this.setorRepository = setorRepository;
        this.userRepository = userRepository;
        this.usuarioSetorRepository = usuarioSetorRepository;
        this.fileStorageService = fileStorageService;
        this.anexoRepository = anexoRepository;
    }

    public Setor criarSetor(CriarSetorDTO data) {
        if (setorRepository.findByNome(data.nome()).isPresent()) {
            throw new AppException("Setor com esse nome já existe", HttpStatus.CONFLICT);
        }
        Setor novoSetor = new Setor();
        novoSetor.setNome(data.nome());
        novoSetor.setDescricao(data.descricao());
        novoSetor.setCreatedAt(LocalDateTime.now());
        novoSetor.setUpdatedAt(LocalDateTime.now());
        return setorRepository.save(novoSetor);
    }

    @Transactional
    public UsuarioSetor associarUsuario(Integer idSetor, AssociarUsuarioSetorDTO data) {
        Setor setor = buscarPorId(idSetor);

        User usuarioParaAssociar = userRepository.findById(data.idUsuario())
                .orElseThrow(() -> new AppException("Usuário não encontrado", HttpStatus.NOT_FOUND));

        User associador = userRepository.findById(data.idUsuarioAssociador())
                .orElseThrow(() -> new AppException("Usuário associador não encontrado", HttpStatus.NOT_FOUND));

        UsuarioSetorId associacaoId = new UsuarioSetorId(usuarioParaAssociar.getIdUsuario(), setor.getIdSetor());
        if (usuarioSetorRepository.existsById(associacaoId)) {
            throw new AppException("Usuário já está associado a esse setor", HttpStatus.CONFLICT);
        }

        UsuarioSetor novaAssociacao = new UsuarioSetor();
        novaAssociacao.setId(associacaoId);
        novaAssociacao.setUsuario(usuarioParaAssociar);
        novaAssociacao.setSetor(setor);
        novaAssociacao.setAssociadoPor(associador);
        novaAssociacao.setAssociadoEm(LocalDateTime.now());

        return usuarioSetorRepository.save(novaAssociacao);
    }

    public void desassociarUsuario(Integer idSetor, Integer idUsuario) {
        UsuarioSetorId associacaoId = new UsuarioSetorId(idUsuario, idSetor);
        if (!usuarioSetorRepository.existsById(associacaoId)) {
            throw new AppException("Associação não encontrada", HttpStatus.NOT_FOUND);
        }
        usuarioSetorRepository.deleteById(associacaoId);
    }

    public List<Setor> listarTodos() {
        return setorRepository.findAll();
    }

    public Setor buscarPorId(Integer idSetor) {
        return setorRepository.findById(idSetor)
                .orElseThrow(() -> new AppException("Setor com ID " + idSetor + " não encontrado", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Setor atualizarSetor(Integer idSetor, CriarSetorDTO data) {
        Setor setorExistente = buscarPorId(idSetor);

        setorRepository.findByNome(data.nome()).ifPresent(setorComMesmoNome -> {
            if (!setorComMesmoNome.getIdSetor().equals(idSetor)) {
                throw new AppException("Nome de setor já está em uso.", HttpStatus.CONFLICT);
            }
        });

        setorExistente.setNome(data.nome());
        setorExistente.setDescricao(data.descricao());
        setorExistente.setUpdatedAt(LocalDateTime.now());

        return setorRepository.save(setorExistente);
    }

    public void deletarSetor(Integer idSetor) {
        Setor setorExistente = buscarPorId(idSetor);
        setorRepository.delete(setorExistente);
    }

    @Transactional
    public Anexo adicionarAnexo(Integer idSetor, MultipartFile file) {
        Setor setor = setorRepository.findById(idSetor)
                .orElseThrow(() -> new AppException("Setor/Empresa não encontrado", HttpStatus.NOT_FOUND));

        String nomeArquivoFisico = fileStorageService.salvarArquivo(file);

        Anexo anexo = new Anexo();
        anexo.setNomeOriginal(file.getOriginalFilename());
        anexo.setNomeArquivo(nomeArquivoFisico);
        anexo.setTipoArquivo(file.getContentType());
        anexo.setSetor(setor);

        return anexoRepository.save(anexo);
    }

    @Transactional
    public void deletarAnexo(Integer idAnexo) {
        Anexo anexo = anexoRepository.findById(idAnexo)
                .orElseThrow(() -> new AppException("Anexo não encontrado", HttpStatus.NOT_FOUND));

        if (anexo.getSetor() == null) {
            throw new AppException("Este anexo não pertence a um setor.", HttpStatus.BAD_REQUEST);
        }

        String nomeFisico = anexo.getNomeArquivo();
        anexoRepository.delete(anexo);
        fileStorageService.deletarArquivo(nomeFisico);
    }
}
