import 'package:flutter/material.dart';
import 'api_service.dart';
import 'task.dart';
import 'task_detail.dart';

class ToDoHomePage extends StatefulWidget {
  const ToDoHomePage({super.key});

  @override
  State<ToDoHomePage> createState() => _ToDoHomePageState();
}

class _ToDoHomePageState extends State<ToDoHomePage> {
  final ApiService _apiService = ApiService();
  List<Task> _tasks = [];
  final TextEditingController _titleController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _fetchTasks();
  }

  Future<void> _fetchTasks() async {
    try {
      final tasks = await _apiService.getTasks();
      setState(() {
        _tasks = tasks;
      });
    } catch (e) {
      print(e);
    }
  }

  void _addTask() async {
    try {
      final newTask = TaskDetail(title: _titleController.text, description: '');
      final createdTask = await _apiService.createTask(newTask);
      setState(() {
        _tasks.add(createdTask);
        _titleController.clear();
      });
    } catch (e) {
      print(e);
    }
  }

  void _updateTask(Task task) async {
    try {
      final updatedTask = await _apiService.updateTask(task);
      setState(() {
        final index = _tasks.indexWhere((t) => t.id == task.id);
        _tasks[index] = updatedTask;
      });
    } catch (e) {
      print(e);
    }
  }

  void _deleteTask(int id) async {
    try {
      await _apiService.deleteTask(id);
      setState(() {
        _tasks.removeWhere((task) => task.id == id);
      });
    } catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Task Manager'),
      ),
      body: Column(
        children: [
          Expanded(child: _buildTaskList()),
          _buildTaskInput(),
        ],
      ),
    );
  }

  Widget _buildTaskList() {
    return ListView.builder(
      itemCount: _tasks.length,
      itemBuilder: (context, index) {
        final task = _tasks[index];
        return Container(
          color: index % 2 == 0 ? Colors.grey[200] : Colors.white,
          child: ListTile(
            leading: Checkbox(
              value: task.completed,
              onChanged: (value) {
                setState(() {
                  task.completed = value!;
                });
                _updateTask(task);
              },
            ),
            title: Text(
              task.title,
              style: TextStyle(
                decoration: task.completed
                    ? TextDecoration.lineThrough
                    : TextDecoration.none,
                color: task.completed ? Colors.grey : Colors.black,
              ),
            ),
            trailing: IconButton(
              icon: const Icon(Icons.delete),
              onPressed: () => _deleteTask(task.id),
            ),
          ),
        );
      },
    );
  }

  Widget _buildTaskInput() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: ListTile(
        shape: RoundedRectangleBorder(
          side: const BorderSide(color: Colors.black, width: 1),
          borderRadius: BorderRadius.circular(5),
        ),
        title: TextField(
          controller: _titleController,
          decoration: const InputDecoration(
            labelText: 'New Task',
          ),
        ),
        trailing: IconButton(
          icon: const Icon(Icons.add),
          onPressed: _addTask,
        ),
      ),
    );
  }
}
