package seven;

import util.DailyTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TaskSeven extends DailyTask<List<String>, List<String>> {
    public TaskSeven() {
        super(InputSeven.TASK_ONE, InputSeven.TASK_TWO);
    }

    @Override
    public String solveTaskOne(List<String> parsed) {
        Directory start = null;
        Directory currentDir = null;

        for (String s : parsed) {
            if (s.startsWith("$ cd ")) {
                String args = s.substring(5);

                if (start == null) {
                    start = new Directory(args);
                    currentDir = start;

                } else {
                    if (args.equals("..")) {
                        currentDir = currentDir.getPrevDir();
                    } else {
                        currentDir = (Directory) currentDir.getDirElements().get(args);
                    }
                }
            } else if (!s.startsWith("$ ls")) {
                String[] split = s.split(" ");

                if (split[0].equals("dir")) {
                    Directory dir = new Directory(split[1]);
                    currentDir.addDir(dir);

                } else {
                    currentDir.addFile(new File(split[1], Long.parseLong(split[0])));
                }
            }
            // Else ls command
        }

        return start.getTaskOneSize() + "";
    }

    @Override
    public List<String> parseInputOne(List<String> input) {
        return input;
    }

    @Override
    public String solveTaskTwo(List<String> parsed) {
        // Tracking a list of all dirs to allow easier traversing
        List<Directory> allDirs = new ArrayList<>();
        Directory start = null;
        Directory currentDir = null;

        for (String s : parsed) {
            if (s.startsWith("$ cd ")) {
                String args = s.substring(5);

                if (start == null) {
                    start = new Directory(args);
                    currentDir = start;
                    allDirs.add(currentDir);
                } else {
                    if (args.equals("..")) {
                        currentDir = currentDir.getPrevDir();
                    } else {
                        currentDir = (Directory) currentDir.getDirElements().get(args);
                    }
                }
            } else if (!s.startsWith("$ ls")) {
                String[] split = s.split(" ");

                if (split[0].equals("dir")) {
                    Directory dir = new Directory(split[1]);
                    currentDir.addDir(dir);
                    allDirs.add(dir);

                } else {
                    currentDir.addFile(new File(split[1], Long.parseLong(split[0])));
                }
            }
            // Else ls command
        }

        long totalFree = 70000000L - start.getDirSize();
        long totalNeeded = 30000000 - totalFree;

        Directory toDell = null;

        for (Directory dir : allDirs) {
            long dirSize;
            if ((dirSize = dir.getDirSize()) >= totalNeeded) {
                if (toDell != null) {
                    if (dirSize < toDell.getDirSize()) {
                        toDell = dir;
                    }
                } else {
                    toDell = dir;
                }
            }
        }

        return toDell.getDirSize() + "";
    }

    @Override
    public List<String> parseInputTwo(List<String> input) {
        return input;
    }

    // Generic FileSystemObject
    static class FileSystemObject {
        private final String name;

        public FileSystemObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // File, has a name & a size
    static class File extends FileSystemObject {
        private final long size;

        public File(String name, long size) {
            super(name);
            this.size = size;
        }
    }

    // Directory, contains files | other directories denoted via their name
    static class Directory extends FileSystemObject {
        private final HashMap<String, FileSystemObject> dirElements;
        private Directory prevDir;

        public Directory(String name) {
            super(name);
            this.dirElements = new HashMap<>();
            this.prevDir = null;
        }

        public void addDir(Directory dir) {
            this.dirElements.put(dir.getName(), dir);
            dir.prevDir = this;
        }

        public void addFile(File file) {
            this.dirElements.put(file.getName(), file);
        }

        public Directory getPrevDir() {
            return prevDir;
        }

        public HashMap<String, FileSystemObject> getDirElements() {
            return dirElements;
        }

        public long getDirSize() {
            long result = 0;

            for (FileSystemObject value : dirElements.values()) {
                if (value instanceof Directory) {
                    result += ((Directory) value).getDirSize();
                } else {
                    result += ((File) value).size;
                }
            }

            return result;
        }

        public long getTaskOneSize() {
            AtomicLong l = new AtomicLong();
            long tmp;

            if ((tmp = getDirSize()) <= 100000) {
                l.addAndGet(tmp);
            }

            this.dirElements.values().forEach(e -> {
                if (e instanceof Directory) {
                    l.getAndAdd(((Directory) e).getTaskOneSize());
                }
            });

            return l.get();
        }
    }
}
