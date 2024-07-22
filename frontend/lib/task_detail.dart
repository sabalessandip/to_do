class TaskDetail {
  final String title;
  final String description;
  bool completed;

  TaskDetail({
    required this.title,
    required this.description,
    this.completed = false,
  });

  Map<String, dynamic> toJson() {
    return {
      'title': title,
      'description': description,
      'completed': completed,
    };
  }
}
