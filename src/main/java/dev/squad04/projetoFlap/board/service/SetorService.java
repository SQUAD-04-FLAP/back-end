package dev.squad04.projetoFlap.board.service;

import dev.squad04.projetoFlap.auth.entity.User;
import dev.squad04.projetoFlap.auth.repository.UserRepository;
import dev.squad04.projetoFlap.board.dto.setor.AssociarUsuarioSetorDTO;
import dev.squad04.projetoFlap.board.dto.setor.CriarSetorDTO;
import dev.squad04.projetoFlap.board.entity.Setor;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetor;
import dev.squad04.projetoFlap.board.entity.associations.UsuarioSetorId;
import dev.squad04.projetoFlap.board.repository.SetorRepository;
import dev.squad04.projetoFlap.board.repository.UsuarioSetorRepository;
import dev.squad04.projetoFlap.exceptions.AppException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SetorService {

    private final SetorRepository setorRepository;
    private final UserRepository userRepository;
    private final UsuarioSetorRepository usuarioSetorRepository;

    public SetorService(SetorRepository setorRepository, UserRepository userRepository, UsuarioSetorRepository usuarioSetorRepository) {
        this.setorRepository = setorRepository;
        this.userRepository = userRepository;
        this.usuarioSetorRepository = usuarioSetorRepository;
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
        Setor setor = setorRepository.findById(idSetor)
                .orElseThrow(() -> new AppException("Setor não encontrado", HttpStatus.NOT_FOUND));

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
}
