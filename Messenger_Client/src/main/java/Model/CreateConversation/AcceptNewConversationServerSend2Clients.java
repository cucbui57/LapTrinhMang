package Model.CreateConversation;

import Model.Participant;

public class AcceptNewConversationServerSend2Clients {
    private Participant participant;

    public AcceptNewConversationServerSend2Clients(Participant participant) {
        this.participant = participant;
    }

    public Participant getParticipant() {

        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
