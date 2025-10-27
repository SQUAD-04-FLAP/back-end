package dev.squad04.projetoFlap.board.mapper;

import dev.squad04.projetoFlap.board.dto.quadro.QuadroResponseDTO;
import dev.squad04.projetoFlap.board.dto.quadro.WorkflowStatusDTO;
import dev.squad04.projetoFlap.board.entity.Quadro;
import dev.squad04.projetoFlap.board.entity.Setor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuadroMapper {

    public QuadroResponseDTO toDTO(Quadro quadro) {
        Set<WorkflowStatusDTO> statusDto = quadro.getWorkflowStatus().stream()
                .map(status -> new WorkflowStatusDTO(
                        status.getIdStatus(),
                        status.getNome(),
                        status.getOrdem()
                ))
                .collect(Collectors.toSet());

        Setor setor = quadro.getSetor();

        Integer idSetor = (setor != null) ? setor.getIdSetor() : null;
        String nomeSetor = (setor != null) ? setor.getNome() : null;

        return new QuadroResponseDTO(
                quadro.getIdQuadro(),
                quadro.getNome(),
                idSetor,
                nomeSetor,
                statusDto,
                quadro.getAtivo(),
                quadro.getCreatedAt(),
                quadro.getUpdatedAt()
        );
    }

    public List<QuadroResponseDTO> toDTOList(List<Quadro> quadros){
        return quadros.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
