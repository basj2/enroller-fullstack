<template>
  <div>
    <form @submit.prevent="addNewMeeting()" v-if="adding">
      <h3>Dodaj nowe spotkanie</h3>
      <label>Nazwa</label>
      <input type="text" v-model="newMeeting.title" />
      <label>Opis</label>
      <textarea v-model="newMeeting.description"></textarea>
      <button>Dodaj</button>
      <span class="error" v-if="error">Spotkanie musi mieć nazwę!</span>
    </form>
    <button @click="adding = true" v-else>Dodaj nowe spotkanie</button>
  </div>
</template>

<script>
export default {
  props: ["message"],
  data() {
    return {
      newMeeting: { participants: [], id: 0 },
      adding: false,
      error: false
    };
  },
  methods: {
    addNewMeeting() {
      this.error = false;
      if (this.newMeeting.title) {
        this.$emit("added", this.newMeeting);
        this.newMeeting = { participants: [], id: 0 };
        this.adding = false;
      } else {
        this.error = true;
      }
    }
  }
};
</script>

<style scoped>
.error {
  color: #f00;
}
</style>
