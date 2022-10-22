import type { Component } from 'solid-js';
import File from './components/File'

const App: Component = () => {

  return (
    <div>
        Hello
      <File/>
    </div>
  );
};
// const file=() => {
//   return (
//     <div>
//         <form>
//           Upload file here.
//           <input type="file" accept=".txt"/>
//           <button type="submit">Upload</button>
//         </form>
//     </div>
//   );
// }
export default App;
