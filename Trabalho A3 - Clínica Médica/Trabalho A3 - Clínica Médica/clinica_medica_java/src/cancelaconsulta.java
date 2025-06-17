// ================ [ CANCELAR CONSULTA ] ================
    private void cancelarConsulta() {
        if (consultas.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nenhuma consulta agendada para cancelar.");
            return;
        }

        // Filtra apenas as consultas futuras ou de hoje que não foram registradas como atendidas
        List<Consulta> consultasAtivas = consultas.stream()
            .filter(c -> c.getDataHora().isAfter(LocalDateTime.now().minusMinutes(1)) && c.getProntuario().isEmpty())
            .collect(Collectors.toList());

        if (consultasAtivas.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Não há consultas ativas para cancelar.");
            return;
        }

        Consulta consultaParaCancelar = (Consulta) JOptionPane.showInputDialog(mainFrame,
                "Selecione a consulta para cancelar:", "Cancelar Consulta",
                JOptionPane.QUESTION_MESSAGE, null,
                consultasAtivas.toArray(), consultasAtivas.get(0));

        if (consultaParaCancelar == null) {
            return; // Usuário cancelou a seleção
        }

        int confirm = JOptionPane.showConfirmDialog(mainFrame,
                "Tem certeza que deseja cancelar esta consulta?\n" + consultaParaCancelar.toString(),
                "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (consultas.remove(consultaParaCancelar)) {
                JOptionPane.showMessageDialog(mainFrame, "Consulta cancelada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Erro ao cancelar a consulta.");
            }
        }
    }