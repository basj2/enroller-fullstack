<template>
  <div>
    <div :class="'alert alert-' + (this.isError ? 'error' : 'success')" v-if="message">{{ message }}</div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>	
    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-if="meetings.length != 0">
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>
	<div v-show="meetings.length != 0">
    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
	</div>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: [],
				message: '',
				isError: false,
            };
        },
		 created() {
			this.$http.get('meetings')
			.then(response => {
			this.meetings = response.data
			})
		},
        methods: {
            addNewMeeting(meeting) {
				this.clearMessage();
				this.$http.post('meetings', meeting)
				.then( response  => {
                        this.success('Spotkanie zostało dodane.');
						this.meetings.push(response.body);
				})						
                .catch(response => this.failure('Błąd przy dodawaniu spotkania. Kod odpowiedzi: ' + response.status));
            },
            addMeetingParticipant(meeting) {
                meeting.participants.push(this.username);
            },
            removeMeetingParticipant(meeting) {
                meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
            },
            deleteMeeting(meeting) {
                this.meetings.splice(this.meetings.indexOf(meeting), 1);
            },
			success(message) {
                this.message = message;
                this.isError = false;
            },
            failure(message) {
                this.message = message;
                this.isError = true;
            },
            clearMessage() {
                this.message = undefined;
            },		
		}			
    }
</script>
